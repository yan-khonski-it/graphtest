/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Number;

/**
 *
 * @author Yan
 */
public class ProcessingNumbers {

    /**
     * C(n, k);
     * @param N
     * @param K
     * @return
     */
    public static long C_n_k(int N, int K) {
        if ((K==0)||(K==N))
            return 1;
	if (K==1)
            return N;
	int c = 1;
	if (N-K<K)
            K=N-K;

	for (int k = K; k >= 1; k--) {
            c = c * (N - K + k) / k;
        }
	return c;
    }
}
