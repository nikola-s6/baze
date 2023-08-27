# prediktorske su numericke a izlazna je faktorska

data <- read.csv("travel-times.csv", stringsAsFactors = F)

data$Date <- NULL
data$StartTime <- NULL

apply(data, 2, function(x) sum(is.na(x)))
apply(data, 2, function(x) sum(x == "", na.rm = T))
apply(data, 2, function(x) sum(x == " ", na.rm = T))
apply(data, 2, function(x) sum(x == "-", na.rm = T))

data$GoingTo[data$GoingTo == ""] <- NA
table(data$GoingTo)
data$GoingTo[is.na(data$GoingTo)] <- "Work"
data$GoingTo <- as.factor(data$GoingTo)


data$DayOfWeek <- factor(data$DayOfWeek, levels = c("Monday", "Tuesday", "Wednesday", "Thursday", "Friday"))


data$FuelEconomy[data$FuelEconomy == "" | data$FuelEconomy == "-"] <- NA
table(data$FuelEconomy)
data$FuelEconomy <- as.numeric(data$FuelEconomy)
shapiro.test(data$FuelEconomy)
data$FuelEconomy[is.na(data$FuelEconomy)] <- median(data$FuelEconomy, na.rm = T)

data$DayOfWeek <- as.numeric(data$DayOfWeek)
data$GoingTo <- as.numeric(data$GoingTo)

?quantile
perc6 <- quantile(data$Congestion407, 0.6)
data$Take407All <- ifelse(data$Congestion407 < perc6 & data$Comments == "", "Yes", "No")
data$Take407All <- as.factor(data$Take407All)
data$Congestion407 <- NULL
data$Comments <- NULL

# ------------------------------------------------------------------------

# standardizacija

# ovde se ne moraju proveravati outlieri jer mozemo uvek standardizaciju da radimo
# ali je on i ovde radio da bi pokazao jer ce trebati za k means svakako

boxplot(data$MaxSpeed)
boxplot.stats(data$MaxSpeed)

apply(data[,-10], 2, function(x) length(boxplot.stats(x)$out))
# sve varijable imaju outliere tako da za sve moramo da radimo standardizaciju

apply(data[,-10], 2, function(x) shapiro.test(x))
# nijedna nema normalnu raspodelu
# ako nemaju normalu raspodelu onda radimo sa median i IQR
# ako ima normalnu raspodelu onda radimo sa mean i stdev

data.std <- apply(data[,-10], 2, function(x) scale(x, median(x), IQR(x)))
data.std <- as.data.frame(data.std)
# kada radimo pojedinacno kolone onda kazemo as.vector(scale(...))

data.std$Take407All <- data$Take407All
str(data.std)


# ----------------------------------------------------
library(caret)
set.seed(1010)
indexes <- createDataPartition(data.std$Take407All, p = 0.8, list = FALSE)
train.data <- data.std[indexes, ]
test.data <- data.std[-indexes, ]

library(e1071)
library(caret)
numFolds = trainControl(method = "cv", number = 10)
kGrid = expand.grid(.k = seq(from = 3, to = 29, by = 2))
set.seed(1010)
crossvalidation <- train( x = train.data[, -10],
                          y = train.data$Take407All,
                          method = "knn",
                          trControl = numFolds,
                          tuneGrid = kGrid)
crossvalidation
crossvalidation$bestTune$k
plot(crossvalidation)

library(class)
knn.pred <- knn(train = train.data[,-10], # training data without the output (class) variable
          test = test.data[, -10], # test data without the output (class) variable
          cl = train.data$Take407All, # output (class) variable is specified here
          k = crossvalidation$bestTune$k)
# odmah se dobiju predikcije !!!!!!!!


getEvalMetrics <- function(cm) {
  TP <- cm[2,2]
  TN <- cm[1,1]
  FP <- cm[1,2]
  FN <- cm[2,1]
  
  accuracy <- sum(diag(cm)) / sum(cm)
  precision <- TP / (TP + FP)
  recall <- TP / (TP + FN)
  F1 <- (2 * precision * recall) / (precision + recall)
  
  c(Accuracy = accuracy, Precision = precision, Recall = recall, F1 = F1)
}

knn.cm <- table(actual = test.data$Take407All, predicted = knn.pred)
knn.cm

knn.eval <- getEvalMetrics(knn.cm)
knn.eval

















