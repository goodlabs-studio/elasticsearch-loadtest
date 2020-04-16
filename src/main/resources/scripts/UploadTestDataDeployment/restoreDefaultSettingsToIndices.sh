URL="LOCALHOST:9200"
URL="${1}"
ENDDAY=$2


DAY=1
while [ $DAY -le $ENDDAY ]
do
INDEXNAME="bushidodb_networkmetric-truefort1582749893-${DAY}-03-20-t-testbulkdata"
echo "closing index for day ${INDEXNAME} for setting restoration"

curl -X POST "${URL}/${INDEXNAME}/_close?pretty"

echo "updating settings"

curl -X PUT "${URL}/${INDEXNAME}/_settings?pretty" -H 'Content-Type: application/json' -d'
{

    "index.store.preload": [],
	"refresh_interval" : "1s",
	"translog.durability" : "request"
  
}
'

echo "re-opening index for day ${DAY} for setting restoration"
curl -X POST "${URL}/${INDEXNAME}/_open?pretty"

DAY=$(( $DAY + 1 ))

done
