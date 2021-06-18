import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import datastructures.Tree;
public class Core {

	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		Tree t;
		System.out.println("Hello, welcome to test relhm of Tree!");
		System.out.println("No root node! need to create one");
		System.out.println("Enter a value for root node:");
		int data = Integer.parseInt(br.readLine().strip());
		t = new Tree(data);
		System.out.println("Root Node created with value "+data);
		boolean run = true;
		while(run) {
			try {
				System.out.println("Main menu");
				System.out.println("1) Print Node");
				System.out.println("2) Print Left Node");
				System.out.println("3) Print Right Node");
				System.out.println("4) Print Parent Node");
				System.out.println("5) Add left Node");
				System.out.println("6) Add right Node");
				System.out.println("7) Move left");
				System.out.println("8) Move right");
				System.out.println("9) Move parent");
				System.out.println("10) Exit");
				int choice = Integer.parseInt(br.readLine().strip());
				switch (choice) {
				case 1: 
					System.out.println(t.getData());
					break;
				case 2: 
					t.moveLeft();
					System.out.println(t.getData());
					t.moveParent();
					break;
				case 3: 
					t.moveRight();
					System.out.println(t.getData());
					t.moveParent();
					break;
				case 4: 
					t.moveParent();
					System.out.println(t.getData());
					break;
				case 5: 
					System.out.println("Enter a value for the left node:");
					data = Integer.parseInt(br.readLine().strip());
					t.addLeftNode(data);
					break;
				case 6: 
					System.out.println("Enter a value for the right node:");
					data = Integer.parseInt(br.readLine().strip());
					t.addRightNode(data);
					break;
				case 7: 
					t.moveLeft();
					break;
				case 8: 
					t.moveRight();
					break;
				case 9:
					t.moveParent();
					break;
				case 10: 
					run = false;
					break;
				default :
					System.out.println("Invalid choice!");
				}
			}
			catch (Exception e){
				System.out.println(e.toString());
			}
		}
	}

}
