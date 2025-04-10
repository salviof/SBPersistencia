/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.geradorDeId;

import com.super_bits.modulosSB.SBCore.UtilGeral.UtilSBCoreGeradorDeID;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.cep.ItfCidade;
import java.io.Serializable;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

/**
 *
 * @author desenvolvedor
 */
public class GeradorIdCidade implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {

        try {
            ItfCidade cidade = (ItfCidade) object;
            if (cidade == null) {
                throw new UnsupportedOperationException("Enviado objeto nulo");
            }
            if (cidade.getUnidadeFederativa() == null) {
                throw new UnsupportedOperationException("Unidade Federativa não definida para " + cidade.getNome());
            }
            Long id = UtilSBCoreGeradorDeID.gerarIdUnicoLetrasDaString(cidade.getNome() + cidade.getUnidadeFederativa().getSigla());
            cidade.setId(id);
            return id;
        } catch (ClassCastException c) {
            throw new HibernateException("O gerador de Id de cidade precisa implementar ItfCidade", c);
        } catch (Throwable t) {
            throw new HibernateException("Erro gerando Chave da cidade", t);
        }
    }

}
