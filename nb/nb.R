# radi samo sa faktorskim i numerickim ako imaju normalnu raspodelu
# one koje nemaju moraju da se diskretizuju

data <- read.csv("wines.csv", stringsAsFactors = F)

data$X <- NULL
data <- subset(data, (country == "France"| country == "Argentina"| country == "Italy"))

apply(data, 2, function(x) sum(is.na(x)))
apply(data, 2, function(x) sum(x == "", na.rm = T))
apply(data, 2, function(x) sum(x == " ", na.rm = T))
apply(data, 2, function(x) sum(x == "-", na.rm = T))

# price nam je protrebno pa bih tu vrednost zamenio sa medijanom ili srvr u zvisnosti od raspodele
# designation bih izbacio jer ima mnogo nedostajucih vrednosti

data$designation <- NULL

data$winery <- as.factor(data$winery)
data$variety <- as.factor(data$variety)
data$title <- as.factor(data$title)
data$region <- as.factor(data$region)
data$province <- as.factor(data$province)
data$country <- as.factor(data$country)
data$description <- as.factor(data$description)

length(levels(data$title))
data$title <- NULL

length(levels(data$description))
data$description <- NULL

length(levels(data$winery))
data$winery <- NULL


# ja ova dva mozda ne bih uklanjao
length(levels(data$region))
data$region <- NULL

length(levels(data$variety))
data$variety <- NULL

shapiro.test(data$price)
# nema normalnu raspodelu
data$price[is.na(data$price)] <- median(data$price, na.rm = T)

data$points <- as.numeric(data$points)



q1 <- quantile(data$price, 0.25)
data$price_category <- ifelse(data$price <= q1, "cheap", "not_cheap")
data$price_category <- factor(data$price_category, c("cheap", "not_cheap"))
data$price <- NULL


shapiro.test(data$points)
# nema normalnu raspodelu pa mora da se diskretizuje


# ovo nije obavezno nego cisto eto onako
library(ggplot2)
ggplot(data, aes(x = points)) + geom_histogram()


library(bnlearn)
?discretize()
discretized <- discretize(data = as.data.frame(data$points),
                        method = "quantile",
                        breaks = c(5))
summary(discretized)


newData <- as.data.frame(cbind(discretized, data[,c(1,3,4)]))
rm(discretized)
rm(data)

# izmena imena
newData$points <- newData$`data$points`
newData$`data$points` <- NULL
newData <- newData[, c(4,1,2,3)]





library(caret)
set.seed(10)
indexes <- createDataPartition(newData$price_category, p = 0.8, list = FALSE)
train.data <- newData[indexes, ]
test.data <- newData[-indexes, ]


library(e1071)
nb1 <- naiveBayes(price_category ~ .,
                data = train.data)
nb1

nb1.pred <- predict(object = nb1,
                    newdata = test.data,
                    type = "class") # moze i raw za raw podatke i moze se podesiti treshhold
head(nb1.pred)

nb1.cm <- table( actual = test.data$price_category, predicted = nb1.pred)
nb1.cm

calculateEvalMetrics <- function(cm){
  TP <- cm[1,1]
  FP <- cm[2,1]
  TN <- cm[2,2]
  FN <- cm[1,2]
  
  accuracy <- sum(diag(cm)) / sum(cm)
  precision <- TP / (TP + FP)
  recall <- TP / (TP + FN)
  F1 <- (2 * precision * recall) / (precision + recall)
  
  c( Accuracu = accuracy, Precision = precision, Recall = recall, F1 = F1)
}

nb1.eval <- calculateEvalMetrics(nb1.cm)
nb1.eval


# racunanje tresholda


nb2.pred.prob <- predict(object = nb1,
                          newdata = test.data,
                          type = "raw")
head(nb2.pred.prob)

library(pROC)
params <- roc(response = as.numeric(test.data$price_category),
              predictor = nb2.pred.prob[, 1],
              levels = c(2,1))

params$auc

plot.roc(params,
        print.thres = TRUE,
        print.thres.best.method = "youden")

coords <- coords(params,
                  ret = c("accuracy", "spec", "sens", "thr"),
                  x = "local maximas")
coords


treshold <- coords[19,4]
treshold

nb2.pred <- ifelse(nb2.pred.prob[, 1] >= treshold, "cheap", "not_cheap")
nb2.pred <- as.factor(nb2.pred)
nb2.pred

nb2.cm <- table(actual = test.data$price_category, predicted = nb2.pred)
nb2.cm

nb2.eval <- calculateEvalMetrics(nb2.cm)
nb2.eval

rbind(nb1.eval, nb2.eval)



















