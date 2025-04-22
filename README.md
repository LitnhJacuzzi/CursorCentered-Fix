This mod fixes the cursor centering issue that occasionally happens when opening a GUI, e.g. open a container GUI but the cursor doesn't appear from the center which could be dangerous when you need to pull something from a chest or inventory rapidly.

# For Wayland Users
This mod supports centering the cursor on wayland using [ydotool](https://github.com/ReimuNotMoe/ydotool) (an automation tool on  wayland), to enable this feature, you need to configure this tool properly, here are the basic setup steps:
## 1. Install ydotool from source
The latest version of ydotool provides seamless centering experience, however it can only be built and installed from source at the moment (the mod was using an outdated ydotool previously).  

To build an install it, just execute these commands in terminal under any directory:
```
sudo apt install git cmake scdoc

git clone https://github.com/ReimuNotMoe/ydotool.git
cd ydotool
mkdir build
cd build
cmake ..
make -j "$(nproc)"
sudo make install
```
Running `ydotoold -V` to verify your installation.
## 2. Add your user to sudo exceptions
The **only** way to successfully center the cursor using ydotool in Minecraft is running ydotool command with `sudo` privilege. Thus you need to **make your user be able to run sudo command without password**: edit /etc/sudoers, add a line `<yourusername> ALL=(ALL) NOPASSWD:ALL`.
## 3. Specify a move scaling value (Optional)
There is an internal problem of moving cursor on wayland programmatically - the actual moved position is affected by system mouse sensitivity/acceleration value. In other words, **the default centering position is correct only with default mouse sensitivity and mouse acceleration disabled**. Fortunately, the requested position and actual position seem to always satisfy a liner function relationship, so we can use a `double` factor to correct it.

To determine this value, run the game with this mod and open any container GUI, the mod will center the cursor using default scaling value (1.0), then keep your cursor at that position and take out your ruler, put it on your monitor to measure its **absolute** position, record `x` or `y` as `x1` or `y1`. Next, keep the position of your game window and determine the *actual* central point, also measure its absolute position with your ruler, record `x` or `y` as `x2` or `y2`, then the scaling value should be `x2 / x1` or `y2 / y1`. **Note that the game window is better to be small (1/2 screen width/height is ok) and close to top-left to ensure the cursor will not go outside the screen, or its position is not reliable (be constrained to screen border)**. This step can also be done by taking screenshots and measuring the position using a painting software that displays pixel coordinates, which is more accurate.

After determining the scaling value, you can restart the game with a JVM argument `-DcursorMoveScalingValue=<value>` to apply it, this value should be always correct util you change the system mouse sensitivity/acceleration value.

