package com.example.demo.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.Categoria;
import com.example.demo.repository.CategoriaDao;

@Controller
@RequestMapping("/categorias")
public class CategoriasController {

	@Autowired
	private CategoriaDao categoriaDao;
	
	@RequestMapping("/novo")
	public ModelAndView novo(Categoria categoria) {
		ModelAndView mv = new ModelAndView("/categoria/CadastroCategoria");
		mv.addObject("categoria",categoria);
		return mv;
	}
	
	@RequestMapping(value = "/novo", method = RequestMethod.POST)
	public ModelAndView salvar(@Valid Categoria categoria, BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return novo(categoria);
		}
		
		categoriaDao.save(categoria);
		attributes.addFlashAttribute("mensagem", "Categoria salva com sucesso!");
		return new ModelAndView("redirect:/categorias/novo");
	}
	
	@RequestMapping("/pesquisar")
	public ModelAndView listagem() {
		List<Categoria> todasCategorias = categoriaDao.findAll();
		ModelAndView mv = new ModelAndView("/categoria/ListagemCategorias");
		mv.addObject("categorias", todasCategorias);
		return mv;
	}
	
	@RequestMapping("{codigo}")
	public ModelAndView edicao(@PathVariable("codigo") Categoria categoria) {
		ModelAndView mv = new ModelAndView("/categoria/CadastroCategoria"); 
		mv.addObject(categoria);
		
		return mv;
	}

	@RequestMapping(value="/delete/{codigo}", method = RequestMethod.GET)
	public String excluir(@PathVariable Long codigo, RedirectAttributes attributes) {
		categoriaDao.delete(categoriaDao.findOne(codigo));
		attributes.addFlashAttribute("mensagem", "Categoria excluída com sucesso!");

		return "redirect:/categorias/pesquisar";

	}
}
