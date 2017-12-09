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

import com.example.demo.model.Projeto;
import com.example.demo.repository.ProjetoDao;

@Controller
@RequestMapping("/projetos")
public class ProjetosController {

	@Autowired
	private ProjetoDao projetoDao;
	
	@RequestMapping("/novo")
	public ModelAndView novo(Projeto projeto) {
		ModelAndView mv = new ModelAndView("/projeto/CadastroProjeto");
		mv.addObject("projeto",projeto);
		return mv;
	}
	
	@RequestMapping(value = "/novo", method = RequestMethod.POST)
	public ModelAndView salvar(@Valid Projeto projeto, BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return novo(projeto);
		}
		
		projetoDao.save(projeto);
		attributes.addFlashAttribute("mensagem", "Projeto salva com sucesso!");
		return new ModelAndView("redirect:/projetos/novo");
	}
	
	@RequestMapping("/pesquisar")
	public ModelAndView listagem() {
		List<Projeto> todasProjetos = projetoDao.findAll();
		ModelAndView mv = new ModelAndView("/projeto/ListagemProjetos");
		mv.addObject("projetos", todasProjetos);
		return mv;
	}
	
	@RequestMapping("{codigo}")
	public ModelAndView edicao(@PathVariable("codigo") Projeto projeto) {
		ModelAndView mv = new ModelAndView("/projeto/CadastroProjeto"); 
		mv.addObject(projeto);
		
		return mv;
	}

	@RequestMapping(value="/delete/{codigo}", method = RequestMethod.GET)
	public String excluir(@PathVariable Long codigo, RedirectAttributes attributes) {
		projetoDao.delete(projetoDao.findOne(codigo));
		attributes.addFlashAttribute("mensagem", "Projeto excluída com sucesso!");

		return "redirect:/projetos/pesquisar";

	}
}
