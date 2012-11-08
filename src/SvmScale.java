/**
 * The following excerpt is taken from guide.pdf on the libsvm site which
 * explains the general use of and reason for this class:
 * 
 * "Scaling before applying SVM is very important. Part 2 of Sarle’s Neural
 * Networks FAQ Sarle (1997) explains the importance of this and most of
 * considerations also apply to SVM. The main advantage of scaling is to avoid
 * attributes in greater numeric ranges dominating those in smaller numeric
 * ranges. Another advantage is to avoid numerical difficulties during the
 * calculation. Because kernel values usually depend on the inner products of
 * feature vectors, e.g. the linear kernel and the polynomial kernel, large
 * attribute values might cause numerical problems. We recommend linearly
 * scaling each attribute to the range [-1, +1] or [0, 1].
 * 
 * Of course we have to use the same method to scale both training and testing
 * data. For example, suppose that we scaled the first attribute of training
 * data from [-10, +10] to [-1, +1]. If the first attribute of testing data lies
 * in the range [-11, +8], we must scale the testing data to [-1.1, +0.8]. See
 * Appendix B for some real examples."
 * 
 * @author James Humphrey
 */
public class SvmScale
{
   public SvmScale( String fScale )
   {
      lo = hi = 0;
      ///attrRange = new double[][2];

      try
      {
      }
      catch( Exception e )
      {
         e.printStackTrace();
      }
   }

   public String toString()
   {
      String out = "lo=" + lo + " hi=" + hi + "\n";
      for( int i = 0; i < 1; i++ )////NUM_ANCHORS;i++)
      {
         out += ( i + 1 ) + " " + attrRange[i][0] + " " + attrRange[i][1];
      }
      return out;
   }

   public double scaleAttr( int attr, double val )
   {
      return ( ( val - attrRange[attr - 1][0] ) / ( attrRange[attr - 1][1] - attrRange[attr - 1][0] ) ) * ( hi - lo )
            + lo;

   }

   private double lo, hi;
   private double[][] attrRange;
}
