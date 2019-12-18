package com.bdd.pontointeligente

import com.bdd.pontointeligente.documents.Empresa
import com.bdd.pontointeligente.documents.Funcionario
import com.bdd.pontointeligente.enums.PerfilEnum
import com.bdd.pontointeligente.repositories.EmpresaRepository
import com.bdd.pontointeligente.repositories.FuncionarioRepository
import com.bdd.pontointeligente.repositories.LancamentoRepository
import com.bdd.pontointeligente.utils.SenhaUtils
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PontointeligenteApplication

fun main(args: Array<String>) {
	runApplication<PontointeligenteApplication>(*args)
}
