package com.bdd.pontointeligente.services.impl

import com.bdd.pontointeligente.documents.Empresa
import com.bdd.pontointeligente.repositories.EmpresaRepository
import org.springframework.stereotype.Service
import com.bdd.pontointeligente.services.EmpresaService

@Service
class EmpresaServiceImpl(val empresaRepository: EmpresaRepository) : EmpresaService {

    override fun buscarPorCnpj(cnpj: String): Empresa? = empresaRepository.findByCnpj(cnpj)

    override fun persistir(empresa: Empresa): Empresa = empresaRepository.save(empresa)
}