import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Stack;

public class OpgHandler {

	/* file content */
	private ArrayList<Character> content = new ArrayList<Character>();
	private Stack<Character> Operations = new Stack<Character>();
	private Stack<Character> Objects = new Stack<Character>();

	/**
	 * @description file-readin
	 * @param fileName
	 */
	public void readSourceFile(String fileName) {
		File file = new File(fileName);
		Reader reader = null;

		try {
			reader = new InputStreamReader(new FileInputStream(file));

			int charget;
			while ((charget = reader.read()) != -1) {
				if ((char) charget == '\r') break;
				this.content.add((char) charget);
			}

			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @description file content printf
	 */
	public void fileContentPrint() {
		System.out.println("---file---");
		for (Character c : this.content)
			System.out.print(c);
		System.out.println();
	}

	/**
	 * @description init func
	 * @param fileName
	 */
	public void init(String fileName) {
		readSourceFile(fileName);
		this.Operations.push('#');
		this.content.add('#');
	}

	/*
	 * > 	-- 1
	 * < 	-- 2
	 * = 	-- 3
	 * err 	-- 0
	 */
	public void handle() {
		Utils utils = new Utils();

		for (int i = 0; i < this.content.size(); i++) {
			char cur = this.content.get(i);

			if (cur == '#' &&
					this.Operations.peek() == '#' &&
					this.Objects.size() == 1 &&
					this.Objects.peek() == 'E') {
				System.exit(0);
			}

			// 当前读入符号不属于终结符号 -- 不能识别
			if (!utils.isTerminal(cur)) {
				System.out.println("E");
				return;
			}
			else {
				Character top = this.Operations.peek();
//				System.out.println("TOP: " + top);
//				System.out.println("CUR: " + cur);
//				System.out.println(utils.result(top, cur));

				if (utils.result(top, cur)  == 1) {
					// 规约
					if (top == 'i') {
						this.Operations.pop();
						this.Objects.push('E');
						System.out.println("R");
					} else if (top == '*' || top == '+') {
						// 规约失败 -- 二元算符缺失对象
						if (this.Objects.size() < 2) {
							System.out.println("RE");
							return;
						} else {
							char second = this.Objects.pop();
							char first = this.Objects.pop();

							String s = "" + first + top + second;
							if (s.equals("E+E") || s.equals("E*E")) {
								this.Objects.push('E');
								this.Operations.pop();
								System.out.println("R");
							}
						}
					} else if (top == ')') {
						// )的规约需要有(的对应和一个对象
						if (this.Operations.size() < 3 || this.Objects.peek() != 'E') {
							System.out.println("RE");
							return;
						} else {
							this.Operations.pop();
							if (this.Operations.peek() == '(') {
								this.Operations.pop();
								// this.Objects.pop();
								// this.Objects.push('E');
							} else {
								System.out.println("RE");
							}
						}
					}

					i--;
				} else if (utils.result(top, cur)  == 2 || utils.result(top, cur)  == 3) {
					// 移进
					this.Operations.push(cur);
					System.out.println("I" + cur);
				} else if (utils.result(top, cur)  == 0) {
					// 无法比较符号优先关系的栈顶和读入符号
					System.out.println("E");
					return;
				}
			}
		}
	}


	/* Main to run */
	public static void main(String[] args) {
		OpgHandler opgHandler = new OpgHandler();
		opgHandler.init(args[0]);
		// opgHandler.init("in.txt");
		opgHandler.handle();
	}
}
