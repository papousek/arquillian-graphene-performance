#!/bin/bash
SCRIPT_DIR=`dirname $BASH_SOURCE`;
SCRIPT_DIR=`readlink -f $SCRIPT_DIR`;
TESTS="testStaticPureElement testDynamicPureElement testInjectedCssElement testInjectedJQueryElement testStaticPureElements testDynamicPureElements testInjectedCssElements testInjectedJqueryElements"
BROWSERS="firefox htmlunit chrome phantomjs"

mvn clean -f $SCRIPT_DIR/pom.xml;
mkdir -p $SCRIPT_DIR/target/measurements;
mvn install -f $SCRIPT_DIR/pom.xml -DskipTests;

for BROWSER in $BROWSERS; do
	touch $SCRIPT_DIR/target/measurements/$BROWSER;
	echo " ### $BROWSER";
	for I in {1..10}; do
		for TEST in $TESTS; do
			mvn test -P$BROWSER -f $SCRIPT_DIR/pom.xml -Dtest="*#$TEST" | grep "###" >> $SCRIPT_DIR/target/measurements/$BROWSER;
		done
	done
done

echo "browser,test,time" $SCRIPT_DIR/target/measurements/data.csv;
for BROWSER in $BROWSERS; do
	TEST=`cat $SCRIPT_DIR/target/measurements/$BROWSER | awk '{print $3}' | tr -d '[]:' |`
	TIME=`cat $SCRIPT_DIR/target/measurements/$BROWSER | awk '{print $4}'`
	echo "$BROWSER,$TEST,$TIME" >> $SCRIPT_DIR/target/measurements/data.csv; 
done
