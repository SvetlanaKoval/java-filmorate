package ru.yandex.practicum.filmorate.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.BaseIntegrationTest;
import ru.yandex.practicum.filmorate.enums.FriendsStatus;
import ru.yandex.practicum.filmorate.model.UserFriends;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

class FriendsRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private FriendsRepository friendsRepository;

    @Test
    void testFindAllFriendsByUserId() {
        List<UserFriends> friends = friendsRepository.findAllFriendsByUserId(3L);

        assertThat(friends)
            .isNotNull()
            .isNotEmpty()
            .allSatisfy(friend -> assertThat(friend.getUserId()).isEqualTo(3L));
    }

    @Test
    void testSaveNewFriend() {
        Long userId = 1L;
        Long friendId = 2L;

        Long savedUserId = friendsRepository.save(userId, friendId);

        assertThat(savedUserId).isEqualTo(userId);

        List<UserFriends> friends = friendsRepository.findAllFriendsByUserId(userId);
        assertThat(friends)
            .anySatisfy(friend -> {
                assertThat(friend.getFriendId()).isEqualTo(friendId);
                assertThat(friend.getStatus()).isEqualTo(FriendsStatus.UNCONFIRMED);
            });
    }

    @Test
    void testUpdateFriendStatus() {
        Long userId = 1L;
        Long friendId = 2L;

        friendsRepository.save(userId, friendId);
        friendsRepository.updateStatus(userId, friendId, FriendsStatus.CONFIRMED.name());

        List<UserFriends> friends = friendsRepository.findAllFriendsByUserId(userId);
        assertThat(friends)
            .anySatisfy(friend -> {
                assertThat(friend.getFriendId()).isEqualTo(friendId);
                assertThat(friend.getStatus()).isEqualTo(FriendsStatus.CONFIRMED);
            });
    }

    @Test
    void testDeleteFriend() {
        Long userId = 1L;
        Long friendId = 2L;

        friendsRepository.save(userId, friendId);
        boolean deleted = friendsRepository.delete(userId, friendId);

        assertThat(deleted).isTrue();

        List<UserFriends> friends = friendsRepository.findAllFriendsByUserId(userId);
        assertThat(friends)
            .noneSatisfy(friend -> assertThat(friend.getFriendId()).isEqualTo(friendId));
    }

    @Test
    void testDeleteAllFriendsByUser() {
        Long userId = 1L;

        friendsRepository.save(userId, 2L);
        friendsRepository.save(userId, 3L);
        boolean deleted = friendsRepository.deleteAllFriendsByUser(userId);

        assertThat(deleted).isTrue();

        List<UserFriends> friends = friendsRepository.findAllFriendsByUserId(userId);
        assertThat(friends).isEmpty();
    }

    @Test
    void testDeleteAllUsersByFriend() {
        Long friendId = 2L;

        friendsRepository.save(1L, friendId);
        friendsRepository.save(3L, friendId);
        boolean deleted = friendsRepository.deleteAllUsersByFriend(friendId);

        assertThat(deleted).isTrue();

        List<UserFriends> friends1 = friendsRepository.findAllFriendsByUserId(1L);
        List<UserFriends> friends3 = friendsRepository.findAllFriendsByUserId(3L);

        assertThat(friends1)
            .noneSatisfy(friend -> assertThat(friend.getFriendId()).isEqualTo(friendId));
        assertThat(friends3)
            .noneSatisfy(friend -> assertThat(friend.getFriendId()).isEqualTo(friendId));
    }

}