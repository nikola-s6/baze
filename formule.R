# Plotovi za sredjivanje

# numericke
ggplot(dataSub, aes(x = zaPoredjenje, fill = izlazna)) + geom_density(alpha = 0.55) + theme_minimal()
# ili ovako
# ggplot(dataSub, aes(x = zaPordejenje, fill = izlazna)) + geom_bar(position = "fill") + theme_minimal()


# daktorske
ggplot(dataSub, aes(x = zaPoredjenje, fill = izlazna)) + geom_bar(position = 'dodge', width = 0.5) + theme_minimal()
#ili
ggplot(dataSub, aes(x = zaPoredjenje, fill = izlazna)) + geom_bar(position = 'fill', width = 0.5) + theme_minimal()




# PLOTOVI ---------------------------------

# stablo
library(rpart.plot)
rpart.plot(tree1, extra = 104)




# nb
# za prikazivanje raspodele pre diskretizacije
# ovo nije obavezno nego cisto eto onako
library(ggplot2)
ggplot(data, aes(x = points)) + geom_histogram()







# lr
# ovo moze i za kmeans
library(corrplot)
cm <- cor(data)
corrplot(cm, method = "number", type = "upper", diag = F)

# provera linearnosti u odlosu na izlaznu
ggplot(
  newdataset,
  aes(
    x = ulaz,
    y = izlaz
  )
) + geom_line(stat = "smooth", se = F) + geom_point()

library(ggplot2)
ggplot(data = test.data) + geom_density(aes(User_Score_Pred, color = "predicted" )) + geom_density(aes(User_Score, color = "actual"))

graphics.off()
par(mfrow = c(1,1)) # da imamo samo 1 red i 1 kolonu za grafove
par(mfrow = c(2,2)) # da imamo 2 reda i 2 kolone za grafove
plot(lm4)

# R-squared and RMSE:
# Compute R-squared on the test data for a model:
# R-squared = 1 - RSS/TSS, where RSS is the residual sum of squares, and TSS is the total sum of squares
RSS <- sum((lm4.pred - test.data$User_Score)^2)
TSS <- sum((mean(train.data$User_Score) - test.data$User_Score)^2)
RSQR <- 1 - RSS / TSS
RSQR
# Compute Root Mean Squared Error (RMSE) for a model on the test data
# to see how much error we are making with the predictions:
# RMSE = sqrt(RSS/n)
RMSE <- sqrt(RSS / nrow(test.data))
RMSE



# kmeans
data.cm <- cor(data)
library(corrplot)
corrplot.mixed(data.cm)


# za racunanje najboljeg k elbow metodom dva nacina
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

# 1
library(ggplot2)
ggplot(eval.metrics, aes(x = clusters, y = tot.withinss)) + geom_line() + geom_point()

# 2
source("Utility.R")
difference.df <- apply(eval.metrics[,c(2,3)], 2, compute.difference)
difference.df

difference.df <- cbind(eval.metrics$clusters, difference.df)
difference.df







getEvaluationMetrics <- function(cm) {

  TP <- cm[2,2]
  TN <- cm[1,1]
  FP <- cm[1,2]
  FN <- cm[2,1]
  
  accuracy <- sum(diag(cm)) / sum(cm) # tacno predvidjene / sve
  precision <- TP / (TP + FP)      # tacno predvidjenje pozitivne / sve predvidjene pozitivne (prva kolona ili druga u zavisnosti od pozitivne klase)
  recall <- TP / (TP + FN)         # tacno predvidjenje pozitivne / prvi ili drugi red u zavisnosti od pozitivne klase
  F1 <- (2 * precision * recall) / (precision + recall)
  
  c(Accuracy = accuracy, 
    Precision = precision, 
    Recall = recall, 
    F1 = F1) 
}


normalize <- function(x) {
  if (sum(x, na.rm = T) == 0) x
  else ((x - min(x, na.rm = T)) / (max(x, na.rm = T) - min(x, na.rm = T)))
}