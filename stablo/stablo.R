data <- read.csv("travel-times.csv", stringsAsFactors = F)

str(data)
summary(data)


sum(data$FuelEconomy == "")

apply(data, 2, function(x) sum(is.na(x)))
apply(data, 2, function(x) sum(x == "", na.rm = T))
apply(data, 2, function(x) sum(x == "-", na.rm = T))
apply(data, 2, function(x) sum(x == " ", na.rm = T))

# nijedna varijabla nema NA vrednosti
# varijable goingto, fueleconomy i comments imaju 5, 17 i 197 praznih stringova respektivno
#fueleconomy ima 2 "-"

#varijablu comments bismo izbacili iz seta da nam nije potrebna za pravljenje izlazne varijable

table(data$DayOfWeek)
table(data$GoingTo)
table(data$Date)
table(data$StartTime)

# izbacujem starttime i date jer nam nisu potrebne za nas model i nece uticati na izlaznu varijablu
# zato sto imaju mnogo razlicitih vrednosti
data$Date <- NULL
data$StartTime <- NULL

data$DayOfWeek <- as.factor(data$DayOfWeek)
# data$DayOfWeek <- factor(data$DayOfWeek, levels = c("Monday", "Tuesday", "Wednesday", "Thursday", "Friday"))
class(data$DayOfWeek)
levels(data$DayOfWeek)

table(data$GoingTo)
# prop.table(table(data$GoingTo))
data$GoingTo[data$GoingTo == ""] <- "Work"
data$GoingTo <- as.factor(data$GoingTo)
levels(data$GoingTo)
table(data$GoingTo)

data$FuelEconomy[data$FuelEconomy == "" | data$FuelEconomy == "-"] <- NA
data$FuelEconomy <- as.numeric(data$FuelEconomy)

# sada NA vrednosti moramo da popunimo nekim vrednostima
# prvo proveravamo normalnost raspodele
# shapiro-wilk test:
#   ako ima normalnu raspodelu sve vrednosti menjamo u sredju vrednost (mean)
#   ako nema normalnu raspodelu onda sve NA vrednosti menjamo u medijanu (median)
shapiro.test(data$FuelEconomy)
# ako je p vrednost manja od 0,05 onda nema normalnu raspodelu

# meanFuelEco <- mean(data$FuelEconomy)
medianFuelEco <- median(data$FuelEconomy, na.rm = T)
data$FuelEconomy[is.na(data$FuelEconomy)] <- medianFuelEco


str(data)


data$Take407All <- ifelse(data$Congestion407 < 0.61 & data$Comments == "", "Yes", "No")
data$Take407All <- as.factor(data$Take407All)
data$Congestion407 <- NULL
data$Comments <- NULL


library(caret)
set.seed(1010)
indexes <- createDataPartition(data$Take407All, p = 0.8, list = FALSE)
train.data <- data[indexes, ]
test.data <- data[-indexes, ]


# rpart cheatsheet za kreiranje stabla
library(rpart)
tree1 <- rpart(Take407All ~ ., # . to include all variables
                data = train.data,
                method = "class") # decrease both for larger tree

tree1

library(rpart.plot)
rpart.plot(tree1, extra = 104)


tree1.pred <- predict(object = tree1,
                      newdata = test.data,
                      type = "class")
tree1.pred

table(tree1.pred)
table(test.data$Take407All)

tree1.cm <- table(true = test.data$Take407All, predicted = tree1.pred)
tree1.cm

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

tree1.eval <- getEvalMetrics(tree1.cm)
tree1.eval


library(e1071)
library(caret)
numFolds <- trainControl(method = "cv", number = 10) # define <k>-fold cross-validation parameters
cpGrid <- expand.grid(.cp = seq(from = 0.001, to = 0.05, by = 0.001))
set.seed(1010)
crossvalidation <- train( x = train.data[, -10],
                          y = train.data$Take407All,
                          method = "rpart",
                          trControl = numFolds,
                          tuneGrid = cpGrid)
crossvalidation
plot(crossvalidation)

bestCp <- crossvalidation$bestTune$cp



tree2 <- rpart(Take407All ~ .,
                data = train.data,
                method = "class",
                control = rpart.control(minsplit = 20, cp = bestCp))

rpart.plot(tree2, extra = 104)

tree2.predict <- predict(object = tree2,
                        newdata = test.data,
                        type = "class")
table(tree2.predict)

tree2.cm <- table(actual = test.data$Take407All, predicted = tree2.predict)
tree2.cm

tree2.eval <- getEvalMetrics(tree2.cm)
tree2.eval



data.frame(rbind(tree1.eval, tree2.eval), row.names = c("Tree 1", "Tree 2"))









