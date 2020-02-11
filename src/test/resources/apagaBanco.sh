#!/bin/bash
source ./SBProjeto.prop
mysqladmin -u root -psenhaDev#123 drop $NOME_BANCO -f
