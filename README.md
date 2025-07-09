![image](https://github.com/user-attachments/assets/58e2c481-03cc-4c4a-b11f-b96954acc86f)
🛠️ GIỚI THIỆU ỨNG DỤNG G-CODE EDITOR – TRÌNH BIÊN TẬP & QUẢN LÝ G-CODE CHUYÊN NGHIỆP
🔷 1. Giới thiệu chung
G-code Editor là một phần mềm soạn thảo mã G-code chuyên dụng được phát triển bằng ngôn ngữ Java, với giao diện người dùng thân thiện và dễ sử dụng. Ứng dụng hỗ trợ các thao tác cần thiết cho người lập trình gia công CNC như: viết, chỉnh sửa, chèn mã nhanh, tô màu cú pháp, phân tích nội dung và trích xuất thông tin lập trình.

Phần mềm này phù hợp với cả người học lập trình CNC, kỹ sư vận hành máy CNC, và kỹ thuật viên lập trình CAM. Với khả năng hỗ trợ đầy đủ các định dạng G-code phổ biến, đây là công cụ không thể thiếu để tối ưu hóa quá trình lập trình và kiểm tra trước khi xuất file ra máy gia công.

🔧 2. Chức năng chính
✏️ Soạn thảo mã G-code:
Vùng nhập liệu lớn, dễ nhìn, có hỗ trợ màu cú pháp cho các lệnh như: G, M, X, Y, Z, A, B, F, S,…

Hỗ trợ chỉnh sửa thủ công hoặc dán mã G-code từ bên ngoài vào.

Tự động làm sạch mã (ví dụ: xóa ký tự N và số dòng).

🔘 Chèn mã G/M-code tự động:
Hơn 20 nút lệnh G/M phổ biến được hiển thị trong giao diện.

Nhấn vào bất kỳ nút nào sẽ tự động chèn mã tương ứng vào con trỏ đang nhập.

Tooltip giải thích rõ ý nghĩa từng mã (ví dụ: G00 – Định vị dao nhanh, M03 – Trục xoay thuận chiều).

🔍 Phân tích nội dung:
Phân tích toàn bộ nội dung văn bản theo dòng.

Hiển thị bảng “Nội dung hiện tại” gồm số dòng và nội dung G-code tương ứng.

Trích xuất và hiển thị các thông số kỹ thuật trong bảng "Thông tin chương trình".

📊 Thống kê tự động:
Trích xuất:

Tên chương trình (PROGRAM NAME)

Tên chi tiết (PART NAME)

Tên file tùy chọn (OPTION FILE)

Đường kính dao (TOOL DIA)

Chiều dài dao (LENGTH)

Tốc độ cắt (F giá trị lớn nhất)

Giá trị lớn nhất & nhỏ nhất của các trục: X, Y, Z, A, B.

💾 Lưu và xuất file:
Hỗ trợ lưu văn bản dưới dạng .gcode, .tap.

Khi lưu, tự động thay thế các ký tự đặc biệt (ví dụ: A → E1, B → E0 để phù hợp định dạng máy CNC).

Cho phép mở file từ menu hoặc kéo-thả trực tiếp vào ứng dụng.

🔄 Làm mới và xóa:
Nút “Làm mới thông tin” để cập nhật lại bảng phân tích nếu có chỉnh sửa nội dung.

Nút “Xóa văn bản” để dọn sạch khung soạn thảo và bảng thống kê.

🖥️ 3. Giao diện người dùng
Giao diện chính được chia thành 3 phần chính:

📝 Vùng soạn thảo (bên trái):
Cho phép nhập hoặc dán mã G-code.

Có định dạng tô màu để dễ phân biệt lệnh và tham số.

🎛️ Khung chèn mã G/M-code (bên phải):
Dạng nút nhấn, tự động thêm vào văn bản đang nhập.

Bao gồm các lệnh thường gặp như: G00, G01, G02, G03, M03, M30, G95, G96, M29, v.v.

📑 Bảng thống kê (bên dưới):
Bảng “Nội dung hiện tại”: Phân tích nội dung từng dòng, hiển thị rõ ràng.

Bảng “Thông tin chương trình”: Tổng hợp các thông tin từ nội dung đã nhập như tên chương trình, dụng cụ, tốc độ cắt, v.v.

👨‍🏫 4. Đối tượng sử dụng
Phần mềm phù hợp với các đối tượng sau:

Sinh viên ngành Cơ khí/CAM/CNC: Học lập trình G-code cơ bản đến nâng cao.

Kỹ thuật viên vận hành máy CNC: Cần công cụ đơn giản để viết và kiểm tra mã trước khi chạy.

Lập trình viên CAM: Muốn chỉnh sửa nhanh mã sau khi xuất từ phần mềm CAM (PowerMill, Mastercam…).

Giáo viên hướng dẫn học viên: Dùng để minh họa rõ ràng từng lệnh và dòng mã.

🧪 5. Giải thích mã nguồn chính
Dưới đây là các thành phần chính của mã nguồn Java:

🔸 JTextPane editorPane
Vùng soạn thảo chính. Sử dụng StyledDocument để hỗ trợ định dạng và tô màu.

🔸 Map<String, String> codes
Lưu trữ các mã G/M-code và mô tả. Dùng để khởi tạo nút lệnh và tooltip.

🔸 JTable currentContentTable
Hiển thị nội dung đã nhập thành bảng: dòng số và nội dung dòng đó.

Cập nhật mỗi khi người dùng thay đổi văn bản.

🔸 JTable programInfoTable
Phân tích nội dung văn bản bằng Regex để tìm thông tin kỹ thuật:

Ví dụ: PROGRAM NAME\\s*:\\s*(\\S+) để tìm tên chương trình.

Tìm giá trị lớn nhất của F, X, Y, Z và cực trị A, B.

🔸 Timer debounceTimer
Giúp giảm số lần cập nhật bảng phân tích mỗi khi người dùng đang gõ liên tục.

Sau khi ngừng gõ 300ms, nội dung sẽ được cập nhật.

🔸 ActionListener
Gắn cho từng nút lệnh G/M-code để tự động chèn đoạn mã tương ứng vào vị trí con trỏ.

🔸 Drag-and-Drop
Hỗ trợ kéo-thả file .gcode, .tap từ ngoài vào vùng soạn thảo để mở nhanh.

⚙️ 6. Công nghệ sử dụng
Ngôn ngữ chính: Java

Giao diện người dùng: Swing

Xử lý dữ liệu: Regular Expression (Regex)

Tô màu cú pháp: StyledDocument & StyleContext

Tương thích: Windows (có thể đóng gói thành .exe)

📦 7. Đóng gói và triển khai
Có thể biên dịch và đóng gói thành .exe thông qua Launch4j hoặc JSmooth.

Dễ dàng chạy độc lập trên Windows, không cần cài đặt môi trường Java (nếu đóng gói kèm JRE).

Có thể cung cấp thêm bản .jar chạy chéo nền tảng (Windows/Linux/Mac).

📘 8. Định hướng phát triển
Thêm tính năng giả lập đường chạy dao (hiển thị visual path).

Cho phép tạo và lưu mẫu G-code để tái sử dụng.

Thêm tùy chọn xuất báo cáo PDF từ nội dung chương trình.

Hỗ trợ cấu hình mã G/M-code tùy theo từng loại máy CNC.

✅ 9. Kết luận
G-code Editor là công cụ gọn nhẹ, dễ sử dụng, nhưng mạnh mẽ, giúp người dùng thao tác với mã G-code một cách dễ dàng, nhanh chóng và chính xác. Dù bạn là người mới học hay kỹ sư có kinh nghiệm, phần mềm này đều mang lại trải nghiệm hiệu quả trong công việc lập trình CNC.
