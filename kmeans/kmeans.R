data <- read.csv("world-happiness-report-2016.csv", stringsAsFactors = F)


length(unique(data$Country))
data$Country <- NULL

length(unique(data$Region))
data$Region <- NULL

apply(data, MARGIN = 2, FUN = function(x) sum(is.na(x)))
apply(data, MARGIN = 2, FUN = function(x) sum(x == "-", na.rm = T))
apply(data, MARGIN = 2, FUN = function(x) sum(x == "", na.rm = T))
apply(data, MARGIN = 2, FUN = function(x) sum(x == " ", na.rm = T))

# freedom ima jednu "-" vrednost i regieon ima jednu "" vrednost

shapiro.test(data$Freedom)
# nema normalnu raspodelu
data$Freedom[is.na(data$Freedom)] <- median(data$Freedom, na.rm = T)


apply(data, 2, function(x) length(boxplot.stats(x)$out))

boxplot(data$Government.Trust) # ima outliere samo velike vrednosti
boxplot(data$Dystopia.Residual) # ima outliere i velike i male vrednosti

library(DescTools)
?Winsorize

Government.Trust.w <- Winsorize(data$Government.Trust, probs = c(0, 0.925))
boxplot.stats(Government.Trust.w)
data$Government.Trust <- Government.Trust.w

Dystopia.Residuals.w <- Winsorize(data$Dystopia.Residual, probs = c(0.05, 0.95))
boxplot.stats(Dystopia.Residuals.w)
data$Dystopia.Residual <- Dystopia.Residuals.w

normalize <- function(x) {
  if (sum(x, na.rm = T) == 0) x
  else ((x - min(x, na.rm = T)) / (max(x, na.rm = T) - min(x, na.rm = T)))
}

data <- as.data.frame(apply(data, 2,normalize))

data.cm <- cor(data)
library(corrplot)
corrplot.mixed(data.cm)

data$Happiness.Rank <- NULL
data$Happiness.Score <- NULL
data$Lower.Confidence.Interval <- NULL
data$Upper.Confidence.Interval <- NULL


eval.metrics <- data.frame()

for (k in 2 : 8) {
  set.seed(1010)
  km <-kmeans(x = data,
               centers = k,
               iter.max = 20,
               nstart = 1000)
  eval.metrics <- rbind(eval.metrics, c(k, km$tot.withinss, (km$betweenss / km$totss)))
}

eval.metrics

colnames(eval.metrics) <- c("clusters", "tot.withinss", "ratio")
eval.metrics

library(ggplot2)
ggplot(eval.metrics, aes(x = clusters, y = tot.withinss)) + geom_line() + geom_point()

source("Utility.R")
difference.df <- apply(eval.metrics[,c(2,3)], 2, compute.difference)
difference.df

difference.df <- cbind(eval.metrics$clusters, difference.df)
difference.df


km <-kmeans(x = data,
            centers = 3,
            iter.max = 20,
            nstart = 1000)
km


sum.stats <- summary.stats(data, km$cluster, 3)
sum.stats



