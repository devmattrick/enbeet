# enbeet
enbeet is a modern Java NBT library. Its codebase is intentionally small and easy to understand. It makes extensive use 
of the Optional API to make null handling much more explicit and safer.

## Installation
Enbeet is on Maven Central, so you don't need to include any repositories to use it. Simply add the following to your 
pom.xml:
```xml
<dependency>
    <groupId>org.mattrick</groupId>
    <artifactId>enbeet</artifactId>
    <version>1.0.2</version>
</dependency>
```

## Usage
```java
// Reading NBT
NBTReader reader = new NBTReader(new FileInputStream("example.nbt"));
NBTCompound compound = reader.read();

// Getting a value
System.out.println(compound.getString("test").get());

// Getting a nested value. Each level is a separate argument.
// Note that keys can contain dots unlike other libraries.
System.out.println(compound.getString("nested value", "test.Value", "aaaa").get());

// Setting values
// Note that the type matters here. ints will be written as IntTag, etc...
compound.set(10, "some", "nested", "key");

// Writing NBT
NBTWriter writer = new NBTWriter(new FileOutputStream("output.nbt"));
writer.write(compound);
```
