package com.bdd.pontointeligente.services.impl

import com.bdd.pontointeligente.documents.Funcionario
import com.bdd.pontointeligente.repositories.FuncionarioRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import com.bdd.pontointeligente.services.FuncionarioService

@Service
class FuncionarioServiceImpl(val funcionarioRepository: FuncionarioRepository) : FuncionarioService {

    override fun persistir(funcionario: Funcionario): Funcionario = funcionarioRepository.save(funcionario)

    override fun buscarPorCpf(cpf: String): Funcionario? = funcionarioRepository.findByCpf(cpf)

    override fun buscarPorEmail(email: String): Funcionario? = funcionarioRepository.findByEmail(email)

    /*findOne foi descontinuado, usar findById*/
    override fun buscarPorId(id: String): Funcionario? = funcionarioRepository.findByIdOrNull(id)
}