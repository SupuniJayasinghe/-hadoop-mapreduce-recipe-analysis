
# 🍽️ Hadoop Ingredient Frequency Analysis

Analyze the most commonly used ingredients in a massive recipe dataset using **Hadoop MapReduce** running on **WSL (Windows Subsystem for Linux)**.

---

## 📘 Project Overview

This project implements a Hadoop MapReduce job in Java to process a **2.3GB+ CSV recipe dataset**. The task is to identify and count the **most frequently used ingredients**, based on the column:
```
NER Ingredients but without amount, brand and such (Organized)
```

It is designed for coursework submission for the **Cloud Computing (EC7205)** module at the **University of Ruhuna**.

---

## 🛠️ Tech Stack

- ✅ Windows 10/11 with WSL
- ✅ Ubuntu (via WSL)
- ✅ OpenJDK 11
- ✅ Apache Hadoop 2.10.2
- ✅ Java (MapReduce API)
- ✅ HDFS for input/output
- ✅ 2.3GB dataset from [Kaggle](https://www.kaggle.com/datasets/wilmerarltstrmberg/recipe-dataset-over-2m)

---

## 🚀 Step-by-Step Guide

### 1️⃣ Install WSL + Ubuntu

In **PowerShell** (as admin):

```powershell
wsl --install
```

Then open Ubuntu from the Start menu and set up your username/password.

---

### 2️⃣ Install Java

In the Ubuntu terminal:

```bash
sudo apt update
sudo apt install openjdk-11-jdk -y
java -version
```

---

### 3️⃣ Download and Set Up Hadoop

```bash
cd ~
wget https://downloads.apache.org/hadoop/common/hadoop-2.10.2/hadoop-2.10.2.tar.gz
tar -xvzf hadoop-2.10.2.tar.gz
mv hadoop-2.10.2 hadoop
```

---

### 4️⃣ Configure Environment Variables

Edit your `.bashrc`:

```bash
nano ~/.bashrc
```

Add:

```bash
export HADOOP_HOME=~/hadoop
export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64
export PATH=$PATH:$HADOOP_HOME/bin:$HADOOP_HOME/sbin
```

Apply:

```bash
source ~/.bashrc
```

---

### 5️⃣ Set JAVA_HOME in Hadoop

```bash
nano ~/hadoop/etc/hadoop/hadoop-env.sh
```

Set:

```bash
export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64
```

---

### 6️⃣ Format HDFS and Start Hadoop

```bash
hdfs namenode -format
start-dfs.sh
start-yarn.sh
```

💡 If SSH fails:

```bash
sudo apt install openssh-server -y
ssh-keygen -t rsa -P ""
cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys
chmod 600 ~/.ssh/authorized_keys
```

Then restart:

```bash
start-dfs.sh
start-yarn.sh
```

---

## 📂 Project Structure

```bash
~/ingredient-count/
├── IngredientFrequency.java     # MapReduce code
├── ingredientcount.jar         # Compiled JAR
├── top_ingredients.txt         # (optional) output
```

---

### 7️⃣ Write the MapReduce Code

Create the source file:

```bash
cd ~
mkdir ingredient-count
cd ingredient-count
nano IngredientFrequency.java
```

📌 The full Java MapReduce code is provided in this repository as IngredientFrequency.java.

### 8️⃣ Compile and Package

```bash
javac -classpath $(hadoop classpath) -d . IngredientFrequency.java
jar -cvf ingredientcount.jar *.class
```

---

## 📥 Dataset

### 9️⃣ Download Dataset from Kaggle

[Kaggle Recipe Dataset Over 2M](https://www.kaggle.com/datasets/wilmerarltstrmberg/recipe-dataset-over-2m)

Save it to:

```bash
/mnt/c/Users/YourUsername/Documents/Cloud/recipes_data.csv
```

Copy to WSL:

```bash
mkdir ~/datasets
cp "/mnt/c/Users/YourUsername/Documents/Cloud/recipes_data.csv" ~/datasets/
```

---

### 🔟 Upload to HDFS

```bash
hdfs dfs -mkdir -p /data_input
hdfs dfs -put ~/datasets/recipes_data.csv /data_input/
```

Verify:

```bash
hdfs dfs -ls /data_input
```

---

## ▶️ Run the MapReduce Job

```bash
hadoop jar ingredientcount.jar IngredientFrequency /data_input /output
```

If `/output` exists:

```bash
hdfs dfs -rm -r /output
```

---

### 📊 View Results

```bash
hdfs dfs -cat /output/part-r-00000 | sort -k2 -nr | head -20
```

Sample Output:
```
sugar     454288
onion     430754
eggs      425854
garlic    395201
butter    363527
...
```

---

## 📤 Export Output File (Optional)

```bash
hdfs dfs -get /output/part-r-00000 ~/top_ingredients.txt
cp ~/top_ingredients.txt /mnt/c/Users/YourUsername/Documents/
```

---

## 🔁 GitHub Setup (Optional)

```bash
cd ~/ingredient-count
git init
git add .
git commit -m "Initial commit for Hadoop ingredient frequency project"
git branch -M main
git remote add origin https://github.com/your-username/hadoop-ingredient-frequency.git
git push -u origin main
```

---

## 📝 Credits

- Dataset from Kaggle (by Wilmer Arltströmberg)
- Developed as part of Cloud Computing Assignment (University of Ruhuna)
