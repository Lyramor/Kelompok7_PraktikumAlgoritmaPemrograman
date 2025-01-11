package controller;

import model.UserModel;
import view.HalamanAwalView;
import view.JenisSampahView;
import model.JenisMapper;
import model.JenisModel;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import util.MyBatisUtil;
import java.util.List;

public class JenisSampahController {
    private JenisSampahView view;
    private SqlSessionFactory factory;

    public JenisSampahController(SqlSessionFactory factory) {
        this.factory = factory;
        loadAndDisplayJenisSampah();
    }

    private void loadAndDisplayJenisSampah() {
        try (SqlSession session = factory.openSession()) {
            JenisMapper mapper = session.getMapper(JenisMapper.class);
            List<JenisModel> jenisList = mapper.getAllJenis();

            // Create view with the jenis list
            this.view = new JenisSampahView(jenisList);

            // Add back button listener
            this.view.addBackButtonListener(e -> {
                view.dispose();
                UserModel dummyModel = new UserModel();
                HalamanAwalView halamanAwalView = new HalamanAwalView();
                UserController userController = new UserController(dummyModel, halamanAwalView);
                userController.openHalamanUtamaView();
            });
        } catch (Exception e) {
            e.printStackTrace();
            // If there's an error loading data, show empty view
            this.view = new JenisSampahView(null);
        }
    }

    public void showJenisSampah() {
        view.setVisible(true);
    }
}