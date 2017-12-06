package com.example.demo.controller;

import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.Licao;
import com.example.demo.model.TipoLicao;
import com.example.demo.repository.LicaoDao;
import com.example.demo.service.LicaoService;

@Controller
@RequestMapping("/licoes")
public class LicoesController {

	@Autowired
	private LicaoDao licaoDao;
	
	@Autowired
	private LicaoService licaoService;
	
	private static final String CADASTRO_VIEW = "/licao/CadastroLicao";
	
	@RequestMapping("/novo")
	public ModelAndView novo(Licao licao) {
		ModelAndView mv = new ModelAndView("/licao/CadastroLicao");
		mv.addObject("licao",licao);
		return mv;
	}
	
	@RequestMapping(value = "/novo", method = RequestMethod.POST)
	public ModelAndView salvar(@Valid Licao licao, BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return novo(licao);
		}
		
		licaoDao.save(licao);
		attributes.addFlashAttribute("mensagem", "Lição salva com sucesso!");
		return new ModelAndView("redirect:/licoes/novo");
	}
	
	@RequestMapping("/pesquisar")
	public ModelAndView listagem() {
		List<Licao> todasLicoes = licaoDao.findAll();
		ModelAndView mv = new ModelAndView("/licao/ListagemLicoes");
		mv.addObject("licoes", todasLicoes);
		return mv;
	}
	
	@RequestMapping("{codigo}")
	public ModelAndView edicao(@PathVariable("codigo") Licao licao) {
		ModelAndView mv = new ModelAndView("/licao/CadastroLicao"); 
		mv.addObject(licao);
		
		return mv;
	}

	@RequestMapping(value="/delete/{codigo}", method = RequestMethod.GET)
	public String excluir(@PathVariable Long codigo, RedirectAttributes attributes) {
		licaoDao.delete(licaoDao.findOne(codigo));
		attributes.addFlashAttribute("mensagem", "Lição excluída com sucesso!");

		return "redirect:/licoes/pesquisar";

	}
	
	/*@RequestMapping(value="/editar/{codigo}", method = RequestMethod.GET)
	public String editar(@PathVariable Long codigo, RedirectAttributes attributes, ModelMap modelMap) {
		modelMap.put("licao", licaoDao.findOne(codigo));
		//licaoDao.delete(licaoDao.findOne(codigo));
		attributes.addFlashAttribute("mensagem", "Lição editada com sucesso!");

		return "redirect:/licoes/novo";

	}*/
	
	@ModelAttribute("todosTipoLicao")
	public List<TipoLicao> todosTipoLicao() {
		return Arrays.asList(TipoLicao.values());
	}
}
