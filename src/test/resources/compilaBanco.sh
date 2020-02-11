#!/bin/bash
source ./SBProjeto.prop
mysqldump -u root -psenhaDev#123  $NOME_BANCO > $NOME_BANCO.Homologacao.sql
#mysqldump -u root -psenhaDev#123 $NOME_BANCO > $NOME_BANCO.Homologacao.sql

