# samo sa numerickim i izlazna je numericka

data <- read.csv("Video_Games_Sales_2017_reduced.csv", stringsAsFactors = F)
str(data)

data <- subset(data, data$Platform == "PS2" | data$Platform == "PS3" | data$Platform == "PS4")


apply(data, MARGIN = 2, FUN = function(x) sum(is.na(x)))
apply(data, MARGIN = 2, FUN = function(x) sum(x == "", na.rm = T))
apply(data, MARGIN = 2, FUN = function(x) sum(x == "N/A", na.rm = T))
apply(data, MARGIN = 2, FUN = function(x) sum(x == "-", na.rm = T))
apply(data, MARGIN = 2, FUN = function(x) sum(x == " ", na.rm = T))

length(unique(data$Name))
data$Name <- NULL

length(unique(data$Genre))

length(unique(data$Publisher))
data$Publisher <- NULL

length(unique(data$Developer))
data$Developer <- NULL

length(unique(data$Year_of_Release))

length(unique(data$Rating))


data$Genre <- NULL
data$Rating <- NULL


apply(data, MARGIN = 2, FUN = function(x) sum(is.na(x)))
apply(data, MARGIN = 2, FUN = function(x) sum(x == "", na.rm = T))
apply(data, MARGIN = 2, FUN = function(x) sum(x == "N/A", na.rm = T))
apply(data, MARGIN = 2, FUN = function(x) sum(x == "-", na.rm = T))
apply(data, MARGIN = 2, FUN = function(x) sum(x == " ", na.rm = T))

data$User_Score[data$User_Score == ""] <- NA
data$Year_of_Release[data$Year_of_Release == "N/A"] <- NA


data$Critic_Score <- as.numeric(data$Critic_Score)
shapiro.test(data$Critic_Score)
data$Critic_Score[is.na(data$Critic_Score)] <- median(data$Critic_Score, na.rm = T)

data$Critic_Count <- as.numeric(data$Critic_Count)
shapiro.test(data$Critic_Count)
data$Critic_Count[is.na(data$Critic_Count)] <- median(data$Critic_Count, na.rm = T)

data$User_Count <- as.numeric(data$User_Count)
shapiro.test(data$User_Count)
data$User_Count[is.na(data$User_Count)] <- median(data$User_Count, na.rm = T)

data$User_Score <- as.factor(data$User_Score)
data$User_Score <- as.numeric(data$User_Score)
shapiro.test(data$User_Score)
data$User_Score[is.na(data$User_Score)] <- median(data$User_Score, na.rm = T)

data$Platform <- as.numeric(as.factor(data$Platform))
data$Year_of_Release <- as.numeric(as.factor(data$Year_of_Release))
shapiro.test(data$Year_of_Release)
data$Year_of_Release[is.na(data$Year_of_Release)] <- median(data$Year_of_Release, na.rm = T)

library(corrplot)
cm <- cor(data)
corrplot(cm, method = "number", type = "upper", diag = F)




library(caret)
set.seed(1010)
indexes <- createDataPartition(data$User_Score, p = 0.8, list = FALSE)
train.data <- data[indexes, ]
test.data <- data[-indexes, ]

ggplot(
  newdataset,
  aes(
    x = ulaz,
    y = izlaz
  )
) + geom_line(stat = "smooth", se = F) + geom_point()

lm1 <- lm(User_Score ~ Critic_Score, data = train.data)
summary(lm1)


# library(car)
# sqrt(vif(lm1))
# ne moze jer ima samo jedna promenljiva u modelu

lm2 <- lm(User_Score ~ ., data = train.data)
summary(lm2)

library(car)
sort(sqrt(vif(lm2)))

lm3 <- lm(User_Score ~ . -Global_Sales, data = train.data)
summary(lm3)
sort(sqrt(vif(lm3)))

lm4 <- lm(User_Score ~ Platform + NA_Sales + EU_Sales + Critic_Score, data = train.data)
summary(lm4)
sort(sqrt(vif(lm4)))

graphics.off()
par(mfrow = c(1,1)) # da imamo samo 1 red i 1 kolonu za grafove
par(mfrow = c(2,2)) # da imamo 2 reda i 2 kolone za grafove
plot(lm4)


lm4.pred <- predict(lm4, newdata = test.data)
head(lm4.pred)

test.data$User_Score_Pred <- lm4.pred


library(ggplot2)
ggplot(data = test.data) + geom_density(aes(User_Score_Pred, color = "predicted" )) + geom_density(aes(User_Score, color = "actual"))


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
