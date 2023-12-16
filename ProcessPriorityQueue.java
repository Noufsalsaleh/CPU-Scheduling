public class ProcessPriorityQueue {
	public Node<Process> head;

	public ProcessPriorityQueue() {
		head = null;
	}

	public boolean isEmpty() {
		return head == null;
	}

	public void enqueue(Process p) {
		Node<Process> newNode = new Node<Process>(p);
		if (isEmpty())
			head = newNode;
		else {
			if (p.priority < head.data.priority) {
				newNode.next = head;
				head = newNode;
			} else if (head.data.priority == p.priority
					&& head.data.arrivalTime > p.arrivalTime && p.executedTime == 0) {
				newNode.next = head;
				head = newNode;
			} else {
				Node<Process> temp = head;
				while (temp.next != null && temp.next.data.priority <= p.priority) {
					if (temp.data.priority == p.priority
							&& temp.data.arrivalTime > p.arrivalTime && p.executedTime == 0)
						break;
					temp = temp.next;
				}

				if (temp.next != null) {
					newNode.next = temp.next;
					temp.next = newNode;
				} else {
					temp.next = newNode;
				}
			}
		}
	}

	public Process dequeue() {
		if (isEmpty())
			return null;
		else {
			Process p = head.data;
			head = head.next;
			return p;
		}
	}

	public Process peek() {
		if (isEmpty())
			return null;
		else {
			return head.data;
		}
	}
}

class Node<T> {
	public T data;
	public Node<T> next;

	public Node(T data) {
		this.data = data;
		this.next = null;
	}
}