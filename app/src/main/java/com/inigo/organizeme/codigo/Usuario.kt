package com.inigo.organizeme.codigo

class Usuario {

    var nombre: String = ""
    var email: String = ""
    var password: String = ""

    constructor(nombre: String, email: String, password: String) {
        this.nombre = nombre
        this.email = email
        this.password = password
    }
    constructor() {

    }

}