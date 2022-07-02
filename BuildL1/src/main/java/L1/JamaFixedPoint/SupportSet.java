/*
% Java code for Homotopy Fixed-Point l1-minimization

% Copyright ©2010. The Regents of the University of California (Regents).
% All Rights Reserved. Contact The Office of Technology Licensing,
% UC Berkeley, 2150 Shattuck Avenue, Suite 510, Berkeley, CA 94720-1620,
% (510) 643-7201, for commercial licensing opportunities.

% Written by by Victor Shia, Allen Y. Yang, Department of EECS, University of California,
% Berkeley.

% IN NO EVENT SHALL REGENTS BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
% SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
% ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
% REGENTS HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

% REGENTS SPECIFICALLY DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED
% TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
% PARTICULAR PURPOSE. THE SOFTWARE AND ACCOMPANYING DOCUMENTATION, IF ANY,
% PROVIDED HEREUNDER IS PROVIDED "AS IS". REGENTS HAS NO OBLIGATION TO
% PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS.
*/

package JamaFixedPoint;

public class SupportSet {

	private int tail;
	private int length;
	private int[] set;

	private boolean changed = true;
	private int[] rtn;

	public SupportSet(int length){
		this.set = new int[length];
		this.length = length;
		this.tail = 0;
	}

	public SupportSet(int[] set, int tail){
		this.set    = set;
		this.length = set.length;
		this.tail   = tail;
	}

	public SupportSet copy(){
		return new SupportSet(this.set.clone(), this.tail);
	}

	public int getLength(){
		return tail;
	}

	public int[] getSet(){
		if (changed == false)
			return rtn;
		else{
			rtn = new int[tail];
			for(int i = 0; i < tail; i++){
				rtn[i] = set[i];
			}
			changed = false;
			return rtn;
		}
	}

	public void addElement(int x){
		set[tail] = x;
		tail = tail + 1;
		changed = true;
	}

	public void addElements(int[] x){
		for(int i = 0 ; i < x.length; i++){
			set[tail + i] = x[i];
		}
		tail = tail + x.length;
		changed = true;
	}

	public int getElement(int index){
		if (index < tail){
			return set[index];
		}
		return -1;
	}

	public int[] getElementsFromIndex(int[] index){
		int[] rtn = new int[index.length];
		for(int i = 0; i < index.length; i++){
			rtn[i] = set[index[i]];
		}
		return rtn;
	}

	public void setElement(int index, int value){
		set[index] = value;
		changed = true;
	}

	public void deleteIndex(int index){
		int t       = set[index];
		set[index]  = set[tail-1];
		set[tail-1] = t;
		tail        = tail - 1;
		changed = true;
	}

	public int[] find(int a, int type){
		SupportSet rtn = new SupportSet(length);
		if (type == 1){
			for(int i = 0 ; i < length; i++){
				if (set[i] > a){
					rtn.addElement(i);
				}
			}
		} else if (type == 2){
			for(int i = 0 ; i < length; i++){
				if (set[i] == a){
					rtn.addElement(i);
				}
			}
		} else if (type == 3){
			for(int i = 0 ; i < length; i++){
				if (set[i] < a){
					rtn.addElement(i);
				}
			}
		}
		return rtn.getSet();
	}


	public static SupportSet setDiffAndUnion(SupportSet X, int[] B, int N){
		boolean[] temp = new boolean[N];
		int repeats = 0;
		for(int i = 0 ; i < X.getLength(); i++){
			temp[X.getElement(i)] = true;
		}
		if(B != null){
			for(int i = 0 ; i < B.length; i++){
				if (B[i] >= 0 && B[i] < N){
					if (temp[B[i]] == true)
						repeats += 1;
					temp[B[i]] = true;
				}
			}
		}
		SupportSet rtn;
		rtn = new SupportSet(N - X.getLength());
		for(int i = 0 ; i < N ; i++){
			if (temp[i] == false)
				rtn.addElement(i);
		}
		return rtn;
	}

	public void printSet(){
		for(int i = 0 ; i < tail; i++)
			System.out.print(set[i] + " ");
		System.out.println();
	}
}
