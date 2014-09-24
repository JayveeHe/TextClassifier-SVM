基于SVM的短文本分类研究
=========
###1、训练集来源
搜狗实验室提供的2006年新闻数据，根据TF-IDF值进行特征选取，10大类中每类选取200个特征词
###2、模型准确度
5折法验证71.9%的准确率……求微博数据集！

###3、依赖包
- 使用[@ansjsun](https://github.com/ansjsun) 的[ansj_seg](https://github.com/ansjsun/ansj_seg)进行NLP分词，同时需要他的nlp-lang-0.2.jar工具包
- 依赖[Libsvm](http://www.csie.ntu.edu.tw/~cjlin/libsvm/) Java版
- org.json工具包（已包含在工程中）

###4、注意事项
- 需修改testSVM中的各种文件路径，最主要的是cmdStr，改为自己的libsvm路径
