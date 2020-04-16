URL=${1}
DAY=1

curl -X PUT "${URL}/_template/tmpl_networkmetric?pretty&include_type_name=true" -H 'Content-Type: application/json' --data-binary "@tmpl_networkmetric.json"

while [ $DAY -le 31 ]
do

#echo "deleting previous index..."
#curl -X DELETE "${URL}/bushidodb_networkmetric-truefort1582749893-${DAY}-03-20-t-testbulkdata?pretty"
echo "creating new index..."
curl -X PUT "${URL}/bushidodb_networkmetric-truefort1582749893-${DAY}-03-20-t-testbulkdata?pretty" -H 'Content-Type: application/json' -d'
{
  "settings": {
    "index.store.preload": ["nvd", "dvd", "tim", "doc", "dim"]
  }
}
'
echo "optimizing settings for bulk upload..."
curl -X PUT "${URL}/bushidodb_networkmetric-truefort1582749893-${DAY}-03-20-t-testbulkdata/_settings?pretty" -H 'Content-Type: application/json' -d'
{
    "index" : {
		"refresh_interval" : -1,
		"number_of_replicas" : 0,
        "translog.durability" : "async"
    }
}
'

DAY=$(( $DAY + 1 ))
done
