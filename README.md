# enbeet
enbeet is a modern Java NBT library. Its codebase is intentionally small and easy to understand. It makes extensive use 
of the Optional API to make null handling much more explicit and safer.

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
