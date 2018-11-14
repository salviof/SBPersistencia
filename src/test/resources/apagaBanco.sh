source ./SBProjeto.prop
mysqladmin processlist -u root $NOME_BANCO | \
awk '$2 ~ /^[0-9]/ {print "KILL "$2";"}' | \
mysql -u root
mysqladmin -u root drop $NOME_BANCO -f