TIMESTAMP="${1}"
ID="${2}"
DOCNUMBER="${3}"
SOURCE_DOC="./${4}"
TAG="${5}"
URL="${6}"
DAY="${7}"

OUTPUTFILENAME="outputchunk"
INDEXNAME=bushidodb_networkmetric-truefort1582749893-${DAY}-03-20-t-testbulkdata
DATADIR="data/${ID}-${DAY}"

DAYTWOTAG='DAYTWO'
DAYTWO="$((${DAY} + 1))"

mkdir -p "${DATADIR}"

sed "s/${TAG}/${TIMESTAMP}/g; s/${DAYTWOTAG}/$DAYTWO/g; s/DAY/${DAY}/g " ${4} > "${DATADIR}/${OUTPUTFILENAME}"
cd "${DATADIR}"

for (( COUNT=1; COUNT<=$DOCNUMBER; COUNT++ ))
do

#	curl -X PUT "${URL}/${INDEXNAME}/_bulk?pretty" -H 'Content-Type: application/json' --data-binary @"${OUTPUTFILENAME}"
	curl --silent --output /dev/null -X PUT "${URL}/${INDEXNAME}/_bulk?pretty" -H 'Content-Type: application/json' --data-binary @"${OUTPUTFILENAME}"
done

rm ${OUTPUTFILENAME}

