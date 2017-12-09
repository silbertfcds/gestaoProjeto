package com.example.demo.entidades.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.example.demo.model.Categoria;

@Component
public class CategoriaConverter implements Converter<String, Categoria> {

    @Override
    public Categoria convert(String codigo) {
        if (!StringUtils.isEmpty(codigo)) {
            Categoria categoria = new Categoria();
            categoria.setCodigo(Long.valueOf(codigo));
            
            return categoria;
        }
        return null;
    }

}

