package com.generation.projetofarmacia.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generation.projetofarmacia.model.Produto;
import com.generation.projetofarmacia.repository.CategoriaRepository;
import com.generation.projetofarmacia.repository.ProdutoRepository;

import jakarta.validation.Valid;



@RestController
@RequestMapping("/Produtos")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProdutoController {
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@GetMapping
	public ResponseEntity<List<Produto>> getAll(){
		return ResponseEntity.ok(produtoRepository.findAll());
	}
	
	@GetMapping("/{nome}")
	public ResponseEntity<List<Produto>> getByNome(@PathVariable String nome){
		return ResponseEntity.ok(produtoRepository.findAllByNomeContainingIgnoreCase(nome));
	}
	
	@GetMapping("/id/{id}")
	public ResponseEntity<Produto> getOne(@PathVariable Long id){
		return produtoRepository.findById(id)
				.map(response -> ResponseEntity.ok(response))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());	
	}
	
	@PostMapping
	public ResponseEntity<Produto> postProduto(@Valid @RequestBody Produto produto){
		if(categoriaRepository.existsById(produto.getCategoria().getId()))
			return ResponseEntity.status(HttpStatus.CREATED)
					.body(produtoRepository.save(produto));

		
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria não existe", null);
		
	}
	
	@PutMapping
	public ResponseEntity<Produto> putProduto(@Valid @RequestBody Produto produto){

		if(!produtoRepository.findById(produto.getId()).isEmpty()) {

			if(!produtoRepository.findById(produto.getCategoria().getId()).isEmpty()) {
				return ResponseEntity.ok(produtoRepository.save(produto));
			}
			
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria não existe", null);
		}
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			
	}
	
	@DeleteMapping("/{id}")
	public void deletePost(@PathVariable Long id){
		Optional<Produto> postagem = produtoRepository.findById(id);
		
		if(postagem.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		
		produtoRepository.deleteById(id);
	}

}
