package com.bdd.pontointeligente.controllers

import com.bdd.pontointeligente.documents.Empresa
import com.bdd.pontointeligente.documents.Funcionario
import com.bdd.pontointeligente.dtos.CadastroPJDto
import com.bdd.pontointeligente.enums.PerfilEnum
import com.bdd.pontointeligente.response.Response
import com.bdd.pontointeligente.services.EmpresaService
import com.bdd.pontointeligente.services.FuncionarioService
import com.bdd.pontointeligente.utils.SenhaUtils
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.validation.ObjectError
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api/cadastrar-pj")
class CadastroPjController (val empresaService: EmpresaService,
                            val funcionarioService: FuncionarioService) {

    @PostMapping
    fun cadastrar(@Valid @RequestBody cadastroPJDto: CadastroPJDto,
                  result: BindingResult): ResponseEntity<Response<CadastroPJDto>> {
        val response: Response<CadastroPJDto> = Response<CadastroPJDto>()

        validarDadosExistes(cadastroPJDto, result)
        if (result.hasErrors()) {
            for (erro in result.allErrors) response.erros.add(erro.defaultMessage!!)
            return ResponseEntity.badRequest().body(response)
        }

        val empresa: Empresa = empresaService.persistir(converterDtoParaEmpresa(cadastroPJDto))

        val funcionario: Funcionario = funcionarioService.persistir(converterDtoParaFuncionario(cadastroPJDto, empresa))

        response.data = converterCadastroPJDto(funcionario, empresa)

        return ResponseEntity.ok(response)

    }

    private fun validarDadosExistes(casdastroPJDto: CadastroPJDto, result: BindingResult) {
        val empresa: Empresa? = empresaService.buscarPorCnpj(casdastroPJDto.cnpj)
        if (empresa != null) {
            result.addError(ObjectError("empresa", "Empresa já existente."))
        }

        val funcionarioCpf: Funcionario? = funcionarioService.buscarPorCpf(casdastroPJDto.cpf)
        if (funcionarioCpf != null) {
            result.addError(ObjectError("funcionário", "CPF já existente."))
        }

        val funcionarioEmail: Funcionario? = funcionarioService.buscarPorEmail(casdastroPJDto.email)
        if (funcionarioEmail != null) {
            result.addError(ObjectError("funcionário", "Email já existente."))
        }
    }

    private fun converterDtoParaEmpresa(cadastroPJDto: CadastroPJDto): Empresa =
            Empresa(cadastroPJDto.razaoSocial, cadastroPJDto.cnpj)

    private fun converterDtoParaFuncionario(casdastroPJDto: CadastroPJDto, empresa: Empresa) =
            Funcionario(casdastroPJDto.nome, casdastroPJDto.email,
                    SenhaUtils().gerarBcrypt(casdastroPJDto.senha), casdastroPJDto.cpf,
                    PerfilEnum.ROLE_ADMIN, empresa.id.toString())

    private fun converterCadastroPJDto(funcionario: Funcionario, empresa: Empresa): CadastroPJDto =
            CadastroPJDto(funcionario.nome, funcionario.email, "", funcionario.cpf, empresa.cnpj, empresa.razaoSocial, funcionario.id)
}