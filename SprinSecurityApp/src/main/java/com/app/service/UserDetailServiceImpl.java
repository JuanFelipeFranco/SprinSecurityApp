package com.app.service;

import com.app.persistence.entities.UserEntity;
import com.app.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //0. SPRING SECURITY OBTIENE LOS USUARIOS DE LA BASE DE DATOS; como el repositorio devuelve un optional utilizamos en que si no esta el username va devolver un error.
        UserEntity userEntity = userRepository.findUserEntityByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario "+username+" no existe"));

        //lista de autorizacion
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

        //1. TOMAMOS LOS ROLES DEL USUARIO y LOS CONVERTIMOS A SIMPLEGRAND AUTORITY QUE ES LO QUE ENTIENDE SPRING SECURITY.  con api stream, con esto estamos tomando los roles del usuario y lo convertimos a simplegrantedAutority para que spring sepa que esta autorizado.
        userEntity.getRoles()
                .forEach(role -> authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name()))));

        //2. TOMAMOS LOS PERMISOS DE ESOS ROLES Y LOS ESTAMOS AUTORIZANDO PARA QUE SPRNG SECURITY LOS OBTENGA. obteniendo los permisos, userentity obtiene los roles, y esto roles tienen permiso a los permisos, recorremos cada uno de los roles
        userEntity.getRoles().stream() //vamos a recoorrer cada uno de los roles;
                .flatMap(role->role.getPermissionList().stream()) //cada rol debe recorrer cada permiso de los roles, para eso usamos flatmap
                .forEach(permission->authorityList.add(new SimpleGrantedAuthority(permission.getName()))); //vamos a obtener el permiso y tomamos la lista y creamos el simplegrandauroity, como es un permiso se le puede pasar directamente el nombre de los permisos

        //3.DEVOLVEMOS EL USUARIO EN LENGUAJE QUE ENTIENDE SPRING SECURITY
        return new User(userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.isEnabled(),
                userEntity.isAccountNoExpired(),
                userEntity.isCredentialNoExpired(),
                userEntity.isAccountNoLocked(),
                authorityList);
        //4 DEBEMOS INYECTARLO EN NUESTRO SECURITY CONFIG
    }
}
