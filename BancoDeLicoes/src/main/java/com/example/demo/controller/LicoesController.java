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

import com.example.demo.model.Categoria;
import com.example.demo.model.Licao;
import com.example.demo.model.Projeto;
import com.example.demo.model.TipoLicao;
import com.example.demo.repository.CategoriaDao;
import com.example.demo.repository.LicaoDao;
import com.example.demo.repository.ProjetoDao;
import com.example.demo.service.LicaoService;

@Controller
@RequestMapping("/licoes")
public class LicoesController {

	@Autowired
	private LicaoDao licaoDao;
	
	@Autowired
	private CategoriaDao categoriaDao;	
	
	@Autowired
	private ProjetoDao projetoDao;
	
	@Autowired
	private LicaoService licaoService;
	
	@RequestMapping("/novo")
	public ModelAndView novo(Licao licao) {
		ModelAndView mv = new ModelAndView("/licao/CadastroLicao");
		mv.addObject("licao",licao);
		mv.addObject("categoria", new Categoria());
		mv.addObject("projeto", new Projeto());
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
		return lista.stream()
			.filter(l->l.getProjeto().getCodigo()==(   licao.getProjeto()!=null ? licao.getProjeto().getCodigo() : l.getProjeto().getCodigo()   ) )
			.filter(l->l.getTipo().equals(   licao.getTipo()!=null? licao.getTipo() : l.getTipo()   ))
			.filter(l->l.getCategoria().getCodigo()==(  licao.getCategoria()!=null ? licao.getCategoria().getCodigo() : l.getCategoria().getCodigo()   ) ) 
			.filter(l->l.getAvaliacao().ordinal()==(   licao.getAvaliacao()!=null ? licao.getAvaliacao().ordinal() : l.getAvaliacao().ordinal()   ))
			.collect(Collectors.toList());
	}
	
	
//	public List<Licao> buscaPorFiltros(Licao licao, List<Licao> lista) {
//		if(licao.getProjeto()!=null && licao.getTipo()!=null && licao.getCategoria()!=null) {
//			return lista.stream()
//					.filter(l->l.getTipo().equals(licao.getTipo()) && l.getProjeto().getCodigo()==licao.getProjeto().getCodigo() && l.getCategoria().getCodigo()==licao.getCategoria().getCodigo())
//					.collect(Collectors.toList());
//		}else if(licao.getProjeto()!=null && licao.getTipo()!=null) {
//			return lista.stream()
//					.filter(l->l.getProjeto().getCodigo()==licao.getProjeto().getCodigo() && l.getTipo().equals(licao.getTipo()))
//					.collect(Collectors.toList());
//		}else if(licao.getProjeto()!=null && licao.getCategoria()!=null) {
//			return lista.stream()
//					.filter(l->l.getProjeto().getCodigo()==licao.getProjeto().getCodigo() && l.getCategoria().getCodigo()==licao.getCategoria().getCodigo())
//					.collect(Collectors.toList());
//		}else if(licao.getTipo()!=null && licao.getCategoria()!=null) {
//			return lista.stream()
//					.filter(l->l.getTipo().equals(licao.getTipo()) && l.getCategoria().getCodigo()==licao.getCategoria().getCodigo())
//					.collect(Collectors.toList());
//		}else if(licao.getProjeto()!=null) {
//			return lista.stream()
//					.filter(l->l.getProjeto().getCodigo()==licao.getProjeto().getCodigo())
//					.collect(Collectors.toList());
//		}else if(licao.getTipo()!=null) {
//			return lista.stream()
//					.filter(l->l.getTipo().equals(licao.getTipo()))
//					.collect(Collectors.toList());
//		}else if(licao.getCategoria()!=null) {
//			return lista.stream()
//					.filter(l->l.getCategoria().getCodigo()==licao.getCategoria().getCodigo())
//					.collect(Collectors.toList());
//		}
//		return null;
//	}
	
	@RequestMapping(value = "/buscar", method = RequestMethod.POST)
	public ModelAndView buscar(Licao licao, RedirectAttributes attributes) {
		List<Licao> todasLicoes = licaoDao.findAll();
		List<Licao> filtradas = new ArrayList<>();
		ModelAndView mv = new ModelAndView("/licao/ListagemLicoes");
		
		if(!todasLicoes.isEmpty() && (licao.getProjeto()!=null || licao.getTipo()!=null) || licao.getCategoria()!=null || licao.getAvaliacao()!=null) {
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
	
	@RequestMapping("/rank")
	public ModelAndView rank() {
		List<Licao> todasLicoes = licaoDao.findAll();
		/*todasLicoes.sort(new Comparator<Licao>(){
			@Override
			public int compare(Licao lhs, Licao rhs) {
				return lhs.getAvaliacao().compareTo(rhs.getAvaliacao());
			}
		});*/
		
		todasLicoes.sort((s1, s2)-> s1.getAvaliacao().compareTo(s2.getAvaliacao()));
		
		
		ModelAndView mv = new ModelAndView("/licao/RankLicoes");
		mv.addObject("licoes", todasLicoes);
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
	
	@ModelAttribute("categorias")
	public List<Categoria> categorias() {
		List<Categoria> lista = categoriaDao.findAll();
		return lista;
	}
	
	@ModelAttribute("todosProjeto")
	public List<Projeto> todosProjeto() {
		List<Projeto> lista = projetoDao.findAll();
		return lista;
	}
	
}
