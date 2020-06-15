import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.linear_model import LogisticRegression
import os
import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns
# 解决matplotlib中文问题
from pylab import mpl
from sklearn.preprocessing import StandardScaler
from sklearn.metrics import accuracy_score
from sklearn.metrics import f1_score
from sklearn.metrics import classification_report
from sklearn.tree import DecisionTreeClassifier
from sklearn.model_selection import GridSearchCV
import joblib

if __name__ == '__main__':
    mpl.rcParams['font.sans-serif'] = ['SimHei']  # 指定默认字体
    mpl.rcParams['axes.unicode_minus'] = False  # 解决保存图像是负号'-'显示为方块的问题

    dataset_path = os.path.join("~/s3data/dataset", 'datasets_heart.csv')

    # 读取iris.csv数据
    df = pd.read_csv(dataset_path)

    # 前十条示例数据
    #print(df.head(10))

    #数据处理
    first = pd.get_dummies(df['cp'], prefix="cp")
    second = pd.get_dummies(df['slope'], prefix="slope")
    thrid = pd.get_dummies(df['thal'], prefix="thal")

    df = pd.concat([df,first,second,thrid], axis = 1)
    df = df.drop(columns = ['cp', 'slope', 'thal'])
    #print(df.head(3))

    y = df.target.values
    X = df.drop(['target'], axis=1)
    #X.shape
    #分割数据集，并进行归一化处理
    X_train, X_test, y_train, y_test = train_test_split(X, y, random_state=6)  # 随机种子6

    standardScaler = StandardScaler()
    standardScaler.fit(X_train)
    X_train = standardScaler.transform(X_train)
    X_test = standardScaler.transform(X_test)

    #创建模型——逻辑回归
    log_reg = LogisticRegression()
    model = log_reg.fit(X_train, y_train)

    LogisticRegression(C=1.0, class_weight=None, dual=False, fit_intercept=True,
                       intercept_scaling=1, max_iter=100, multi_class='ovr', n_jobs=1,
                       penalty='l2', random_state=None, solver='liblinear', tol=0.0001,
                       verbose=0, warm_start=False)

    log_reg.score(X_train, y_train)

    log_reg.score(X_test, y_test)

    y_predict_log = log_reg.predict(X_test)
    #计算分类精准度
    #accuracy_score(y_test, y_predict_log)

    #查看F1指标
    #f1_score(y_test,y_predict_log)

    #print(classification_report(y_test, y_predict_log))

    #存放模型
    model_path = os.path.join('../model', 'lf.pkl')

    joblib.dump(model, model_path, compress=3)

    #创建模型——决策树
    dt_clf = DecisionTreeClassifier(random_state=6)

    param_grid = [
        {
            'max_features': ['auto', 'sqrt', 'log2'],
            'min_samples_split': [2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18],
            'min_samples_leaf': [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11]
        }
    ]
    grid_search = GridSearchCV(dt_clf, param_grid)

    model2 = grid_search.fit(X_train, y_train)

    #print(grid_search.best_estimator_)

    dt_clf = grid_search.best_estimator_

    y_predict_dt = dt_clf.predict(X_test)

    #存放模型
    model_path2 = os.path.join('../model', 'dt.pkl')

    joblib.dump(model2, model_path2, compress=3)

    print("finish!")