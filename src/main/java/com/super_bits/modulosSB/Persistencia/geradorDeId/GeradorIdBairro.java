/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.geradorDeId;

import com.super_bits.modulosSB.SBCore.UtilGeral.UtilCRCGeradorDeID;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.cep.ComoBairro;
import java.io.Serializable;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

/**
 *
 * @author desenvolvedorninja01
 * @since 17/10/2019
 * @version 1.0
 */
public class GeradorIdBairro implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {

        try {
            ComoBairro bairro = (ComoBairro) object;
            if (bairro == null) {
                throw new UnsupportedOperationException("Enviado objeto nulo");
            }
            Long id = UtilCRCGeradorDeID.gerarIdUnicoLetrasDaString(bairro.getNome() + bairro.getCidade().getNome() + bairro.getCidade().getUnidadeFederativa().getSigla());
            bairro.setId(id);

            return id;
        } catch (ClassCastException c) {
            throw new HibernateException("O gerador de Id de cidade precisa implementar ComoBairro", c);
        } catch (Throwable t) {
            throw new HibernateException("Erro gerando Chave da cidade", t);
        }
    }
}
