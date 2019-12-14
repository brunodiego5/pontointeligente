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
class PontointeligenteApplication(val empresaRepository: EmpresaRepository,
								  val funcionarioRepository: FuncionarioRepository,
								  val lancamentoRepository: LancamentoRepository) : CommandLineRunner {

	/*CommandLineRunner  = assim que carregar a apliacao será executado o método run()*/

	override fun run(vararg args: String?) {
		empresaRepository.deleteAll()
		funcionarioRepository.deleteAll()
		lancamentoRepository.deleteAll()

		//val empresa: Empresa = Empresa("Empresa", "10443887000146")
		//empresaRepository.save(empresa)

		val empresa: Empresa = empresaRepository.save(Empresa("Empresa", "10443887000146"))
		//empresaRepository.save(empresa)

		val admin: Funcionario = Funcionario("Admin", "admin@empresa.com",
				SenhaUtils().gerarBcrypt("123456"), "25708317000",
				PerfilEnum.ROLE_ADMIN, empresa.id!!) /*" !! => sempre vai existir um id "*/
		funcionarioRepository.save(admin)

		val funcionario: Funcionario = Funcionario("Funcionario",
				"funcionario@empresa.com", SenhaUtils().gerarBcrypt("123456"),
				"44325441557", PerfilEnum.ROLE_USUARIO, empresa.id!!)
		funcionarioRepository.save(funcionario)

		println("Empresa ID: " + empresa.id)
		println("Admin ID: " + admin.id)
		println("Funcionario ID: " + funcionario.id)

	}

}

fun main(args: Array<String>) {
	runApplication<PontointeligenteApplication>(*args)
}
