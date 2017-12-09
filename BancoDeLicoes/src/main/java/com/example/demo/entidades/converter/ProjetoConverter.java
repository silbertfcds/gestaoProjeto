package com.example.demo.entidades.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.example.demo.model.Projeto;

@Component
public class ProjetoConverter implements Converter<String, Projeto>{
	
	@Override
    public Projeto convert(String codigo) {
        if (!StringUtils.isEmpty(codigo)) {
            Projeto projeto = new Projeto();
            projeto.setCodigo(Long.valueOf(codigo));
            
            return projeto;
        }
        return null;
    }
}
