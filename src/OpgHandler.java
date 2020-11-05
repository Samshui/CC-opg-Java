import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Stack;

public class OpgHandler {

	/* file content */
	private ArrayList<Character> content = new ArrayList<Character>();
	private Stack<Character> S = new Stack<Character>();

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
		this.S.push('#');
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

			// 错误处理
			if (!utils.isTerminal(cur)) {
				System.out.println("E");
				return;
			}

			if (this.S.peek() == 'E') {
				/*
				 * #...E*E
				 * #...E+E
				 */
				this.S.pop();
				char top = this.S.peek();

				// 成功退出
				if (top == '#' && cur == '#') return;

				// 错误处理 且不允许在E后读i
				if (!utils.isTerminal(top) || cur == 'i') {
					System.out.println("E");
					return;
				}

				if (utils.result(top, cur) == 1) {
					if (top == '*' || top == '+') {
						this.S.pop();
						if (this.S.peek() != 'E') {
							System.out.println("RE");
							return;
						}
						System.out.println("R");
					} else {
						// 错误处理
						System.out.println("RE");
						return;
					}
					i--;
				} else if (utils.result(top, cur) == 2 || utils.result(top, cur) == 3) {
					this.S.push('E');
					this.S.push(cur);
					System.out.println("I" + cur);
				} else {
					// 错误处理
					System.out.println("RE");
					return;
				}

			} else if (utils.isTerminal(this.S.peek())) {
				/*
				 * #...(E)
				 * #...i
				 */
				char top = this.S.peek();
				if (utils.result(top, cur) == 1) {
					if (top == 'i') {
						this.S.pop();

						// 错误处理 i后不许有E
						if (this.S.peek() == 'E') {
							System.out.println("RE");
							return;
						} else {
							this.S.push('E');
							System.out.println("R");
						}
					} else if (top == ')') {
						this.S.pop(); //)
						char c = this.S.pop();
						if (c == 'E' && this.S.peek() == '(') {
							this.S.pop();
							this.S.push('E');
							System.out.println("R");
						} else {
							System.out.println("RE");
							return;
						}
					} else {
						// 错误处理
						System.out.println("RE");
						return;
					}
					i--;
				} else if (utils.result(top, cur) == 2 || utils.result(top, cur) == 3) {
					this.S.push(cur);
					System.out.println("I" + cur);
				} else {
					// 错误处理
					System.out.println("E");
					return;
				}

			} else {
				// 错误处理
				System.out.println("RE");
				return;
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
