#!/bin/bash
data <- read.csv("./target/measurements/data.csv");

for (browser in unique(data$browser)) {
	browserData <- data[data$browser == browser, ]
	element <- browserData[!grepl("elements", browserData$test),]
	elements <- browserData[grepl("elements", browserData$test),]
	png(paste0("./target/measurements/graphs/", browser, "-element.png"), width=700, height=300);
	par(las=1);
	barplot(element$time, names.arg=element$test, main=paste0(browser, ": one element"), ylab="time in ms", xlab="test");
	dev.off()
	png(paste0("./target/measurements/graphs/", browser, "-elements.png"), width=700, height=300);
	barplot(elements$time, names.arg=elements$test, main=paste0(browser, ": list of elements"), ylab="time in ms", xlab="test");
	dev.off()
}
