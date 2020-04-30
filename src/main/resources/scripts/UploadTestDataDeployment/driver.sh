START=$(($(date +%s%N)/1000000))
THREADS=${1}
URL=${2}
STARTDAY=${3}
ENDDAY=${4}
DOCPERPERIOD=${5}
BASEDOCA=${6}
BASEDOCB=${7}
NUMNODES=${8}

TIMESTAMPTAG="REPLACE_ME"

echo ${START}


for (( DAY=${STARTDAY}; DAY<=${ENDDAY}; DAY++ ))
do

awk 'NR % 2 == 0' timestamps.txt | xargs -P${1} -I % ./generateAndUploadInd.sh % "data-%" "${DOCPERPERIOD}" "${BASEDOCA}" "${TIMESTAMPTAG}" "${URL}" "${DAY}"
wait
awk 'NR % 2 == 1' timestamps.txt | xargs -P${1} -I % ./generateAndUploadInd.sh % "data-%" "${DOCPERPERIOD}" "${BASEDOCB}" "${TIMESTAMPTAG}" "${URL}" "${DAY}"
wait
if [ $NUMNODES -gt 1 ]
then
./restoreReplica.sh "${DAY}"
fi
done

END=$(($(date +%s%N)/1000000))
echo ${END}
echo "took " $(($END-$START)) "ms"
