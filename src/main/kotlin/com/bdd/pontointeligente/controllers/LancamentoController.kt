package com.bdd.pontointeligente.controllers

import com.bdd.pontointeligente.documents.Funcionario
import com.bdd.pontointeligente.documents.Lancamento
import com.bdd.pontointeligente.dtos.LancamentoDto
import com.bdd.pontointeligente.enums.TipoEnum
import com.bdd.pontointeligente.repositories.LancamentoRepository
import com.bdd.pontointeligente.response.Response
import com.bdd.pontointeligente.services.FuncionarioService
import com.bdd.pontointeligente.services.LancamentoService
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.validation.ObjectError
import org.springframework.web.bind.annotation.*
import java.text.SimpleDateFormat
import javax.validation.Valid


@RestController
@RequestMapping("/api/lancamentos")
class LancamentoController(val lancamentoService: LancamentoService,
                           val funcionarioService: FuncionarioService,
                           val lancamentoRepository: LancamentoRepository) {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    @Value("\${paginacao.qtd_por_pagina}")
    val qtdPorPagina: Int = 15

    @PostMapping
    fun adicionar(@Valid @RequestBody lancamentoDto: LancamentoDto,
                  result: BindingResult): ResponseEntity<Response<LancamentoDto>> {
        val response: Response<LancamentoDto> = Response<LancamentoDto>()
        validarFuncioFuncionario(lancamentoDto, result)

        if (result.hasErrors()) {
            for (erro in result.allErrors) response.erros.add(erro.defaultMessage!!)
            return ResponseEntity.badRequest().body(response)
        }

        val lancamento: Lancamento = convertDtoParaLancamento(lancamentoDto, result)
        lancamentoService.persistir(lancamento)
        response.data = converterLancamentoDto(lancamento)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/{id}")
    fun listarPorId(@PathVariable("id") id: String): ResponseEntity<Response<LancamentoDto>> {
        val response: Response<LancamentoDto> = Response<LancamentoDto>()
        val lancamento: Lancamento? = lancamentoService.buscarPorId(id)

        if (lancamento == null) {
            response.erros.add("Lançamento não encontrado para o id $id")

            return ResponseEntity.badRequest().body(response)
        }

        response.data = converterLancamentoDto(lancamento)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/funcinario/{funcionarioId}")
    fun listarPorFuncioanrioId(@PathVariable(value = "funcionarioId") funcionarioId: String,
                               @RequestParam(value = "pag", defaultValue = "0") pag: Int,
                               @RequestParam(value = "ord", defaultValue = "id") ord: String,
                               @RequestParam(value = "dir", defaultValue = "DESC") dir: String):
        ResponseEntity<Response<Page<LancamentoDto>>> {

        val response: Response<Page<LancamentoDto>> = Response<Page<LancamentoDto>>()

        val pageRequest: PageRequest = PageRequest.of(pag, qtdPorPagina, Sort.Direction.valueOf(dir), ord)
        val lancamentos: Page<Lancamento> =
                lancamentoService.buscarPorFuncionarioId(funcionarioId, pageRequest)

        val lancamentoDto: Page<LancamentoDto> =
                lancamentos.map { lancamento -> converterLancamentoDto(lancamento) }

        response.data = lancamentoDto

        return ResponseEntity.ok(response)


    }

    @GetMapping
    fun pesquisar(): List<Lancamento?>? = lancamentoRepository.findAll()

    private fun converterLancamentoDto(lancamento: Lancamento): LancamentoDto =
        LancamentoDto(dateFormat.format(lancamento.data), lancamento.tipo.toString(),
                lancamento.descricao, lancamento.localizacao,
                lancamento.funcionarioId, lancamento.id)


    private fun convertDtoParaLancamento(lancamentoDto: LancamentoDto, result: BindingResult): Lancamento {
        if (lancamentoDto.id != null) {
            val lanc: Lancamento? = lancamentoService.buscarPorId(lancamentoDto.id!!)
            if (lanc == null) result.addError(ObjectError("lancamento", "Lançamento não encontrado"))
        }

        return Lancamento(dateFormat.parse(lancamentoDto.data), TipoEnum.valueOf(lancamentoDto.tipo!!),
                lancamentoDto.funcionarioId!!, lancamentoDto.descricao,
                lancamentoDto.localizacao, lancamentoDto.id)
    }

    private fun validarFuncioFuncionario(lancamentoDto: LancamentoDto, result: BindingResult) {
        if (lancamentoDto.funcionarioId == null) {
            result.addError(ObjectError("funcionario",
                    "Funcionário não informado"))

            return
        }

        val funcionario: Funcionario? = funcionarioService.buscarPorId(lancamentoDto.funcionarioId)
        if (funcionario == null) {
            result.addError(ObjectError("funcionario",
                    "Funcionário não encontrado. ID inexistente."))
        }
    }

}