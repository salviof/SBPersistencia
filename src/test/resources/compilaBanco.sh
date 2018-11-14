source ./SBProjeto.prop
mysqldump -u root  $NOME_BANCO > $NOME_BANCO.Homologacao.sql
