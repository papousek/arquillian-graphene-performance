#!/bin/bash
SCRIPT_DIR=`dirname $BASH_SOURCE`;
SCRIPT_DIR=`readlink -f $SCRIPT_DIR`;

echo "browser,test,time" > $SCRIPT_DIR/target/measurements/data.csv;
for FILE in $SCRIPT_DIR/target/measurements/*; do
	if [[ $FILE =~ .*data\.csv.* ]]; then
		continue;
	fi 
	BROWSER=`basename $FILE`;
	TIMES=`cat $FILE | awk -F ":" '{print $2}' | tr -d 'ms '`;
	for TEST in `cat $FILE | awk -F "[" '{print $2}' | awk -F "]" '{print $1}' | sed 's/ /_/g'`; do
		TEST=`echo $TEST | sed 's/_/ /g'`;
		TIME=`cat $FILE | grep "\[$TEST\]" | awk -F ":" '{print $2}' | tr -d 'ms '`;
		echo "$BROWSER,$TEST,$TIME" >> $SCRIPT_DIR/target/measurements/data.csv;
	done
	 
done

if [ -d $SCRIPT_DIR/target/measurements/graphs ]; then
	rm -rf $SCRIPT_DIR/target/measurements/graphs;
fi
mkdir $SCRIPT_DIR/target/measurements/graphs;
R --vanilla < $SCRIPT_DIR/process-result.R;
