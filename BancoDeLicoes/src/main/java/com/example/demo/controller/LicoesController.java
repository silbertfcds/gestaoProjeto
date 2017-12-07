package com.example.demo.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
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
		
		licaoService.salvar(licao);
		attributes.addFlashAttribute("mensagem", "Lição salva com sucesso!");
		return new ModelAndView("redirect:/licoes/novo");
	}
	
	public List<Licao> buscaPorFiltros(Licao licao, List<Licao> lista) {
		List<Licao> filtrada = new ArrayList<>();
		if(licao.getProjeto()!="" && licao.getTipo()!=null) {
			return filtrada =  lista.stream()
					.filter(l->l.getTipo().equals(licao.getTipo()) && l.getProjeto().toUpperCase().equals(licao.getProjeto().toUpperCase()))
					.collect(Collectors.toList());
		}else if(licao.getTipo()!=null) {
			return filtrada =  lista.stream()
					.filter(l->l.getTipo().equals(licao.getTipo()))
					.collect(Collectors.toList());
		}else if(licao.getProjeto()!="") {
			return filtrada =  lista.stream()
					.filter(l->l.getProjeto().toUpperCase().equals(licao.getProjeto().toUpperCase()))
					.collect(Collectors.toList());
		}
		return null;
	}
	
	@RequestMapping(value = "/buscar", method = RequestMethod.POST)
	public ModelAndView buscar(Licao licao, RedirectAttributes attributes) {
		List<Licao> todasLicoes = licaoDao.findAll();
		List<Licao> filtradas = new ArrayList<>();
		ModelAndView mv = new ModelAndView("/licao/ListagemLicoes");
		
		if(!todasLicoes.isEmpty() && (licao.getProjeto()!="" || licao.getTipo()!=null)) {
			filtradas = buscaPorFiltros(licao, todasLicoes);
			if(filtradas.isEmpty()) {
				attributes.addFlashAttribute("mensagem", "Filtros não retornou resultados");
				return new ModelAndView("redirect:/licoes/pesquisar");
			}
		}else {
			return new ModelAndView("redirect:/licoes/pesquisar");
		}
		
		mv.addObject("licoes", filtradas);
		
		return mv;
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
	
	@ModelAttribute("todosTipoLicao")
	public List<TipoLicao> todosTipoLicao() {
		return Arrays.asList(TipoLicao.values());
	}
}
