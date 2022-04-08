#!/bin/bash
source ./SBProjeto.prop
mysqladmin -u root -psenhaDev#123 create $NOME_BANCO
mysql -u root $NOME_BANCO -psenhaDev#123 < ./$NOME_BANCO.Homologacao.sql