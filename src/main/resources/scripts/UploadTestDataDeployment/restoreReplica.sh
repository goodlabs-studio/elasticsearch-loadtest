URL="LOCALHOST:9200"

DAY=${1}
curl -X PUT "${URL}/bushidodb_networkmetric-truefort1582749893-${DAY}-03-20-t-testbulkdata/_settings?pretty" -H 'Content-Type: application/json' -d'
{
    "index" : {
		"number_of_replicas" : 1
    }
}
'
curl -X POST "localhost:9200/bushidodb_networkmetric-truefort1582749893-${DAY}-03-20-t-testbulkdata/_refresh?pretty"


