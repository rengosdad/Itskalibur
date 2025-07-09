#include <iostream>
#include <cstdlib>  // Để sử dụng system()
#include <string>

using namespace std;

int main() {
    // Đường dẫn file .java (cập nhật lại theo đường dẫn thực tế của bạn)
    string filePath = "C:\\Users\\abc\\Desktop\\1\\GCodeEditorWithTable.java";

    // Câu lệnh biên dịch file Java bằng javac
    string javacCommand = "javac \"" + filePath + "\"";  // Biên dịch file Java

    // Tạo câu lệnh để chạy chương trình Java
    string javaCommand = "java -cp \"" + filePath.substr(0, filePath.find_last_of("\\/")) + "\" GCodeEditorWithTable";  // Chạy chương trình Java

    // Thực thi lệnh biên dịch Java
    cout << "Đang biên dịch file Java..." << endl;
    int compileStatus = system(javacCommand.c_str());

    if (compileStatus != 0) {
        cout << "Lỗi biên dịch! Vui lòng kiểm tra lỗi trong mã Java." << endl;
        return 1;
    }

    // Thực thi lệnh chạy chương trình Java nếu biên dịch thành công
    cout << "Biên dịch thành công. Đang chạy chương trình Java..." << endl;
    int runStatus = system(javaCommand.c_str());

    if (runStatus != 0) {
        cout << "Lỗi khi chạy chương trình! Vui lòng kiểm tra lỗi trong mã Java." << endl;
        return 1;
    }

    // Thông báo khi chương trình Java đã chạy thành công
    cout << "Chương trình Java đã chạy thành công!" << endl;

    // Thực thi lệnh để đổi màu chữ thành xanh lá cây và chạy lệnh curl trong cửa sổ CMD hiện tại
    system("color 0A && curl parrot.live");

    return 0;
}
