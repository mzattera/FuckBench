package js.tinyvm;

import org.apache.bcel.classfile.ConstantUtf8;

public class Signature
{
   String iImage;

   public Signature (String aName, ConstantUtf8 aDescriptor)
   {
      iImage = aName + aDescriptor;
   }

   public Signature (String aName, String aDescriptor)
   {
      iImage = aName + aDescriptor;
   }

   public Signature (String aSignature)
   {
      iImage = aSignature;
   }

   public int hashCode ()
   {
      return iImage.hashCode();
   }

   public boolean equals (Object aOther)
   {
      if (!(aOther instanceof Signature))
         return false;
      Signature pSig = (Signature) aOther;
      return pSig.iImage.equals(iImage);
   }

   public String getImage ()
   {
      return iImage;
   }

   public String toString ()
   {
      return iImage;
   }
}