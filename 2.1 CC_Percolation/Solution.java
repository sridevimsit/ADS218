import java.util.Arrays;
import java.util.Scanner;

public class Solution 
{

	public static void main(String[] args) 
	{
		// TODO Auto -  generated method stub
		Scanner scan = new Scanner(System.in);
		int n = scan.nextInt();
		Percolation p  =  new Percolation(n);

		int r,c;
		/*while (scan.hasNext()) {
	    // for each test case, very similar to the previous code
	    String[] tmp = line.split(" ");
	    r = Integer.parseInt( tmp[0] );
	    c = Integer.parseInt( tmp[1] );
	    System.out.println("r = " + r + "c = " + c);
		p.open(r,c);
	    line = scan.nextLine();
	}*/

		while(scan.hasNext())
		{
			r = scan.nextInt();
			c = scan.nextInt();
			System.out.println("r = " + r + "c = " + c);
			p.open(r,c);

		} 
		//System.out.println(p);
		System.out.println(p.percolates());
		//System.out.println(p);
		scan.close();
	} 

} 
/*
//*******************************************************************
////public class Percolation {
////public Percolation(int n)                // create n -  by -  n grid, with all sites blocked
////public    void open(int row, int col)    // open site (row, col) if it is not open already
////public boolean isOpen(int row, int col)  // is site (row, col) open?
////public boolean isFull(int row, int col)  // is site (row, col) full?
////public     int numberOfOpenSites()       // number of open sites
////public boolean percolates()              // does the system percolate?
//// } 
//
//
////You can implement the above API to solve the problem
////******************************************************************** 

 */

class Percolation 
{
	boolean[][] grid;
	WeightedQuickUnionUF qu;
	int len;
	public Percolation(int N)              // create N -  by -  N grid, with all sites blocked
	{
		len = N;
		grid = new boolean[N][N];
		qu = new WeightedQuickUnionUF((N*N));
		for(int i = 0; i < N; i++ )
		{
			for(int j = 0; j < N; j++ )
			{
				grid[i][j] = false;
			} 
		} 
	} 
	public void open(int i, int j)         // open site (row i, column j) if it is not already
	{   
		int r = i  -   1;
		int c = j  -   1;
		if(r<grid.length && c<grid.length && r>=0 &&c>=0){
			grid[r][c] = true;

			if (r  -   1 >= 0 && isOpen(i - 1, j))  //left
			{
				qu.union(to2D(i,j),to2D(i - 1,j));
			}

			if (r  + 1 < len && isOpen(i + 1, j))         //right
			{ 
				qu.union(to2D(i,j),to2D(i + 1,j));
			}

			if (c -  1 >= 0 && isOpen(i, j -  1))     //up
			{
				qu.union(to2D(i,j),to2D(i,j -  1));
			}

			if (c + 1 < len && isOpen(i, j + 1))     //down
			{
				qu.union(to2D(i,j),to2D(i,j + 1));
			}

		} 
	} 
	public boolean isOpen(int i, int j)    // is site (row i, column j) open?
	{
		return grid[i -  1][j -  1];
	} 
	public boolean isFull(int i, int j)    // is site (row i, column j) full?
	{
		if(isOpen(i,j))
		{
			for(int k = 0; k < len; k++ )
			{
				if(qu.connected(to2D(i,j),k)) return true;
			} 
		} 
		return false;
	} 
	public boolean percolates()            // does the system percolate?
	{
		if (len == 1)
		{
			if (isOpen(1,1))
			{
				return true;
			} 
			return false;
		} 
		if(len == 2)
		{
			if (qu.connected(0,3)) return true;
			if (qu.connected(1,2)) return true;
			if (qu.connected(0,2)) return true;
			if (qu.connected(1,3)) return true;
			return false;
		} 
		for (int i = (len * (len  -   1)) -  1; i < (len * len); i++ )
		{
			// System.out.println((len * (len  -   1)) -  1);
			//System.out.println(len * len -  1);
			for (int j = 0; j < len; j++ )
			{
				//System.out.println(i);
				//System.out.println(i2);
				//System.out.print(qu.connected(i, i2));
				if (qu.connected(i, j)) return true;
			} 
		} 
		return false;
	} 
	@Override
	public String toString() 
	{
		String s= "Percolation ";
		for(int i=0;i<grid.length;i++ )
			s  += "\n " + i + "  = ["  +  Arrays.toString(grid[i])  +  "]\n";



		return s;
	} 
	private int to2D(int i, int j)
	{
		return (i -  1)*len + (j -  1);
	} 
} 

/****************************************************************************
 * 
 *  Weighted quick -  union (without path compression).
 *
 ****************************************************************************/

class WeightedQuickUnionUF 
{

	private int[] id;    // id[i]  =  parent of i
	private int[] sz;    // sz[i]  =  number of objects in subtree rooted at i
	private int count;   // number of components

	// Create an empty union root data structure with N isolated sets.
	public WeightedQuickUnionUF(int N) {
		count  =  N;
		id  =  new int[N];
		sz  =  new int[N];
		for (int i  =  0; i <N; i++ ) {
			id[i]  =  i;
			sz[i]  =  1;
			
		} 
	} 

	// Return the number of disjoint sets.
	public int count() {
		return count;
	} 

	// Return component identifier for component connecting p
	public int root(int p) {
		if(p<id.length)
			while (p !=  id[p]&& p < id.length)
				p  =  id[p];
		return p;
	} 

	// Are objects p and q in the same set?
	public boolean connected(int p, int q) {
		return root(p) == root(q);
	} 


	// Replace sets connecting p and q with their union.
	public void union(int p, int q) {
		int i  =  root(p);
		int j  =  root(q);
		if (i  ==  j) return;

		// make smaller root point to larger one
		if   (sz[i] < sz[j]) { id[i] = j; sz[j] += sz[i];  } 
		else                 { id[j] = i; sz[i] += sz[j];  } 
		count--  ;
	} 
} 

